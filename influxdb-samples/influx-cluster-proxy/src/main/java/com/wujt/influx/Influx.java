package com.wujt.influx;


import com.wujt.influx.config.InfluxDbWriteConfig;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class Influx {
    private static final Logger logger = LoggerFactory.getLogger(Influx.class);

    private static final ThreadFactory SCHEDULE_THREAD_FACTORY = new ScheduleThreadFactory();

    private static final DBFactory LEVEL_DB_FACTORY = new Iq80DBFactory();
    /**
     * 本地缓存最大数量
     */
    private static final int LOCAL_CACHE_STORE_MAX_NUM = 5000;


    private final LongAdder localWriteCount = new LongAdder();

    /**
     * 本地数据缓存，不同于client自身缓存
     */
    private LinkedBlockingQueue<String> dataCache = new LinkedBlockingQueue<>();


    private InfluxDB influxDB;

    /**
     * 所属bucket号
     */
    private Integer bucketNum;

    /**
     * influxdb健康状态，false时不可用
     */
    private volatile boolean alive = true;

    /**
     * local level db
     */
    private volatile DB levelDb;

    private String localStorePath;

    private String url;


    private Influx() {

    }

    /**
     * 初始化influxdb节点管理类
     *
     * @param url
     * @param user
     * @param passwd
     * @return
     */
    public static Influx create(String url, String user, String passwd, Integer bucketNum, String defaultDb,
                                String localStorePath, InfluxDbWriteConfig influxDbWriteConfig) {
        Influx influx = new Influx();
        influx.bucketNum = bucketNum;
        influx.localStorePath = localStorePath;
        influx.url = url;
        influx.influxDB = InfluxDBFactory.connect("http://" + url, user, passwd);
        //初始化默认写db
        influx.influxDB.setDatabase(defaultDb);
        if (influxDbWriteConfig == null) {
            influxDbWriteConfig = new InfluxDbWriteConfig();
        }
        BatchOptions batchOptions = BatchOptions.DEFAULTS
                .actions(influxDbWriteConfig.getAction())
                .bufferLimit(influxDbWriteConfig.getBufferLimit())
                .flushDuration(influxDbWriteConfig.getFlushDuration())
                .jitterDuration(influxDbWriteConfig.getJitterDuration())
                .precision(TimeUnit.MILLISECONDS)
                .exceptionHandler((failedPoints, throwable) -> {
                    //exceptionHandler基本是被周期写进程调用
                    if ((throwable instanceof InfluxDBException)
                            && ((InfluxDBException) throwable).isRetryWorth()) {
                        //内存队列满导致，为可重试的数据点数据，若非数据库问题需要本地缓存
                        boolean shouldAbandon;
                        if (influx.isAlive()) {
                            //刷下节点状态，节点不正常了
                            refreshAliveStatus(influx);
                            shouldAbandon = influx.isAlive();
                        } else {
                            shouldAbandon = false;
                        }
                        if (shouldAbandon) {
                            failedPoints.getPoints().forEach(point -> logger.warn("记录influx插入失败的节点信息,抛弃:\nlineProtocol:" + point.lineProtocol() + "\nurl: " + url, throwable));
                        } else {
                            //本地缓存
                            influx.batchLocalWrite(failedPoints.getDatabase(), failedPoints.getRetentionPolicy(), failedPoints.getPoints());
                        }

                    } else {
                        logger.warn("记录influx插入失败的节点信息,抛弃: failedPoints:{} ", failedPoints, throwable);
                    }
                })
                .threadFactory(SCHEDULE_THREAD_FACTORY);
        influx.influxDB.enableBatch(batchOptions);
        influx.influxDB.enableGzip();
        refreshAliveStatus(influx);
        return influx;
    }

    /**
     * 返回原状态
     *
     * @param influx
     * @return
     */
    private static synchronized boolean refreshAliveStatus(Influx influx) {
        boolean oldStatus = influx.alive;
        try {
            logger.debug("influxdb {} ping.", influx.url);
            influx.alive = influx.influxDB.ping().isGood();
        } catch (Exception e) {
            logger.warn("节点健康检查请求异常，节点将被认为不健康！", e);
            influx.alive = false;
        }
        if (oldStatus != influx.alive) {
            logger.info("influxdb {} status change,old status {},new status {}.", influx.url, oldStatus, influx.alive);
        }
        return oldStatus;
    }

    /**
     * influxdb写操作
     */
    public void write(final String database, final String retentionPolicy, Point point) {
        if (alive) {
            logger.debug("influx write..");
            influxDB.write(database, retentionPolicy, point);
        } else {
            logger.debug("local write..");
            singleLocalWrite(database, retentionPolicy, point);
        }
    }

    private void singleLocalWrite(final String database, final String retentionPolicy, Point point) {
        try {
            localWriteCount.increment();
            dataCache.put(database + "," + retentionPolicy + "##" + point.lineProtocol());
            dataCacheCheckAndWrite();
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            localWriteCount.decrement();
        }
    }

    private void batchLocalWrite(final String database, final String retentionPolicy, List<Point> pointList) {
        try {
            localWriteCount.increment();
            for (Point point : pointList) {
                dataCache.put(database + "," + retentionPolicy + "##" + point.lineProtocol());
            }
            dataCacheCheckAndWrite();
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            localWriteCount.decrement();
        }
    }

    private void dataCacheCheckAndWrite() {
        if (dataCache.size() >= LOCAL_CACHE_STORE_MAX_NUM) {
            //数据暂时刷盘
            List<String> dataList = new ArrayList<>();
            int n = dataCache.drainTo(dataList);
            if (n < LOCAL_CACHE_STORE_MAX_NUM) {
                //其他线程已经获取做刷盘,新写的数量还没达到批量值
                return;
            }
            if (levelDb == null) {
                if (!initLevelDb()) {
                    logger.error("本地levelDb初始化错误,抛弃数据。");
                    return;
                }
            }
            WriteBatch batch = levelDb.createWriteBatch();
            for (String data : dataList) {
                String uuid = UUID.randomUUID().toString();
                batch.put(Iq80DBFactory.bytes(uuid), Iq80DBFactory.bytes(data));
            }
            //执行写入
            levelDb.write(batch);
        }
    }

    private synchronized boolean initLevelDb() {
        if (levelDb == null) {
            Options options = new Options();
            options.createIfMissing(true);
            //folder 是db存储目录
            try {
                levelDb = LEVEL_DB_FACTORY.open(new File(
                        localStorePath + File.separator + url.replaceAll("[.:]", "_")), options);
            } catch (IOException e) {
                logger.error("", e);
                return false;
            }
        }
        return true;
    }

    /**
     * influxdb读操作
     *
     * @param queryString  ,读sql,如:SELECT * FROM cpu WHERE idle > $idle AND system > $system
     *                     参考:https://github.com/influxdata/influxdb-java
     * @param paramsHolder , sql占位符参数
     */
    public QueryResult read(String queryString, Map<String, Object> paramsHolder, String db) {
        BoundParameterQuery.QueryBuilder queryBuilder =
                BoundParameterQuery.QueryBuilder
                        .newQuery(queryString)
                        .forDatabase(db);
        for (Map.Entry<String, Object> entry : paramsHolder.entrySet()) {
            queryBuilder.bind(entry.getKey(), entry.getValue());
        }
        return influxDB.query(queryBuilder.create());
    }

    /**
     * influxdb读操作
     *
     * @param queryCommand 查询语句
     */
    public QueryResult read(Query queryCommand) {
        return influxDB.query(queryCommand);
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * 节点健康度检查
     */
    public void healthCheck() {
        boolean oldStatus = refreshAliveStatus(this);
        if (!oldStatus && alive) {
            while (localWriteCount.intValue() > 0) {
                //存在还在写的线程，等待
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            //节点状态健康恢复,恢复刷盘数据
            List<String> dataList = new ArrayList<>();
            //缓存数据恢复
            dataCache.drainTo(dataList);

            Map<String, List<String>> recoverMap = new HashMap<>();
            for (String data : dataList) {
                recoverDataParse(recoverMap, data);
            }
            recoverMapWrite(recoverMap);
            recoverMap.clear();
            dataList.clear();
            //本地盘数据恢复
            if (levelDb == null) {
                if (!initLevelDb()) {
                    return;
                }
            }
            for (Map.Entry<byte[], byte[]> next : levelDb) {
                recoverDataParse(recoverMap, Iq80DBFactory.asString(next.getValue()));
                if (recoverMap.size() >= 5000) {
                    recoverMapWrite(recoverMap);
                    recoverMap.clear();
                }
            }
            if (!recoverMap.isEmpty()) {
                recoverMapWrite(recoverMap);
            }
            try {
                levelDb.close();
                levelDb = null;
                LEVEL_DB_FACTORY.destroy(new File(
                        localStorePath + File.separator + url.replaceAll("[.:]", "_")), new Options());
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    private void recoverDataParse(Map<String, List<String>> recoverMap, String data) {
        String key = data.substring(0, data.indexOf("##"));
        String val = data.substring(data.indexOf("##") + 2);
        if (!recoverMap.containsKey(key)) {
            recoverMap.put(key, new ArrayList<>());
        }
        recoverMap.get(key).add(val);
    }

    private void recoverMapWrite(Map<String, List<String>> recoverMap) {
        for (Map.Entry<String, List<String>> entry : recoverMap.entrySet()) {
            String key = entry.getKey();
            String db = key.substring(0, key.indexOf(","));
            String retentionPolicy = key.substring(key.indexOf(",") + 1);
            influxDB.write(db, retentionPolicy, InfluxDB.ConsistencyLevel.ONE, entry.getValue());
        }
    }

    public void close() {
        this.influxDB.close();
    }
}
