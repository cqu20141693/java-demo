package com.wujt.influx.cluster;


import com.wujt.influx.Influx;
import com.wujt.influx.InfluxReadOperation;
import com.wujt.influx.InfluxWriteOperation;
import com.wujt.influx.cluster.sharding.ByPassRuleExecutor;
import com.wujt.influx.cluster.sharding.ShardingParam;
import com.wujt.influx.cluster.sharding.ShardingResult;
import com.wujt.influx.cluster.sharding.ShardingRuleFactory;
import com.wujt.influx.config.InfluxDbConfig;
import com.wujt.influx.config.InfluxDbWriteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class InfluxFactory {
    private final static Logger logger = LoggerFactory.getLogger(InfluxFactory.class);

    private Map<Integer, List<Influx>> influxHolder = new HashMap<>();

    private Random random;

    @Autowired
    private InfluxDbConfig influxDbConfig;

    @Autowired
    private ShardingRuleFactory shardingRuleFactory;

    @Autowired
    private ByPassRuleExecutor byPassRuleExecutor;

    @Autowired
    private InfluxDbWriteConfig influxDbWriteConfig;

    @PostConstruct
    public void init() {
        String user = influxDbConfig.getUser();
        String passwd = influxDbConfig.getPassword();
        logger.debug(influxDbConfig.getServersAndBucket());

        for (String serverAndBucket : influxDbConfig.getServersAndBucket().split(",")) {
            String[] data = serverAndBucket.split(":");
            if (data.length != 3) {
                logger.error("influx配置错误");
                throw new RuntimeException("influx配置错误");
            }
            String serverUrl = data[0] + ":" + data[1];
            Integer bucketNum = Integer.valueOf(data[2]);
            if (!influxHolder.containsKey(bucketNum)) {
                influxHolder.put(bucketNum, new ArrayList<>(2));
            }
            influxHolder.get(bucketNum).add(Influx.create(serverUrl, user, passwd, bucketNum,
                    influxDbConfig.getDefaultDataDbName(), influxDbConfig.getLocalStorePath(), influxDbWriteConfig));
        }
        random = new Random(System.currentTimeMillis());
    }

    /**
     * 给出设备对应的写节点操作信息
     *
     * @param shardingParam
     * @return
     */
    InfluxWriteOperation fetchWriteInflux(ShardingParam shardingParam) {
        Assert.notNull(shardingParam, "shardingParam不可为空");
        Assert.notNull(shardingParam.getDeviceId(), "shardingParam的deviceId不可为空");
        Assert.notNull(shardingParam.getProductId(), "shardingParam的productId不可为空");
        //根据shardRule的结果
        ShardingResult shardingResult = shardingRuleFactory.execAllWriteRule(shardingParam);
        if (shardingResult.isSkipWrite()) {
            return InfluxWriteOperation.create();
        }
        return InfluxWriteOperation.create().setInfluxList(influxHolder.get(shardingResult.getShardingBucket()))
                .setDatabase(shardingResult.getDataBase())
                .setRetentionPolicy(shardingResult.getRetentionPolicy());
    }

    /**
     * 给出设备旁路写节点操作信息，主目前用于数据迁移用
     *
     * @param shardingParam
     * @return
     */
    InfluxWriteOperation fetchByPassWriteInflux(ShardingParam shardingParam) {
        //根据shardRule的结果
        ShardingResult shardingResult = byPassRuleExecutor.execByPassRule(shardingParam);
        if (shardingResult == null) {
            return InfluxWriteOperation.create();
        }
        return InfluxWriteOperation.create().setInfluxList(influxHolder.get(shardingResult.getShardingBucket()))
                .setDatabase(shardingResult.getDataBase())
                .setRetentionPolicy(shardingResult.getRetentionPolicy());
    }

    /**
     * 给出可读节点 , 设备级别
     *
     * @param shardingParam
     * @return
     */
    InfluxReadOperation fetchReadInfluxByDeviceId(ShardingParam shardingParam) {
        Assert.notNull(shardingParam, "shardingParam不可为空");
        Assert.notNull(shardingParam.getDeviceId(), "shardingParam的deviceId不可为空");
        Assert.notNull(shardingParam.getProductId(), "shardingParam的productId不可为空");
        //根据shardRule的结果
        ShardingResult shardingResult = shardingRuleFactory.execAllReadRule(shardingParam);

        Influx influx = getAvailableInflux(influxHolder.get(shardingResult.getShardingBucket()));
        return InfluxReadOperation.create().setInflux(influx).setDatabase(shardingResult.getDataBase());
    }

    public Influx getAvailableInfluxByBucket(Integer bucket) {
        List<Influx> influxes = influxHolder.get(bucket);
        return getAvailableInflux(influxes);
    }


    private Influx getAvailableInflux(List<Influx> influxList) {
        if (CollectionUtils.isEmpty(influxList)) {
            return null;
        }
        //随机选一个健康节点
        int randomInt = random.nextInt(influxList.size());
        int selectBucket = randomInt;
        Influx influx = influxList.get(selectBucket);
        if (influx.isAlive()) {
            return influx;
        }
        //轮圈圈去看有没有好的
        do {
            selectBucket = (selectBucket + 1) % influxList.size();
            if (randomInt == selectBucket) {
                //选了一圈了还没有健康的，返回空的
                logger.error("influxdb均不可用了!!");
                influx = null;
                break;
            }
            influx = influxList.get(selectBucket);
        } while (!influx.isAlive());
        return influx;
    }

    /**
     * 给出可读节点 , 产品级别，目前诉求不大，与设备级别一致
     *
     * @param shardingParam
     * @return
     */
    InfluxReadOperation fetchReadInfluxByProductId(ShardingParam shardingParam) {
        Assert.notNull(shardingParam, "shardingParam不可为空");
        Assert.notNull(shardingParam.getProductId(), "shardingParam的productId不可为空");
        //填充值
        shardingParam.setDeviceId(-1L);
        return fetchReadInfluxByDeviceId(shardingParam);
    }

    /**
     * 获取特定的influx用于读
     * 预计错误纠正时可能用到
     *
     * @param shardingParam
     * @param bucketNum
     * @return
     */
    InfluxReadOperation fetchSpecReadInflux(ShardingParam shardingParam, Integer bucketNum) {
        ShardingResult shardingResult = shardingRuleFactory.execAllReadRule(shardingParam);
        Influx influx = getAvailableInflux(bucketNum != null ?
                influxHolder.get(bucketNum) :
                influxHolder.get(shardingResult.getShardingBucket()));
        return InfluxReadOperation.create().setInflux(influx).setDatabase(shardingResult.getDataBase());
    }

    List<InfluxReadOperation> fetchAllInflux() {
        List<InfluxReadOperation> list = new ArrayList<>();
        for (Map.Entry<Integer, List<Influx>> entry : influxHolder.entrySet()) {
            for (Influx influx : entry.getValue()) {
                list.add(InfluxReadOperation.create().setInflux(influx).setDatabase(influxDbConfig.getDefaultDataDbName()));
            }
        }
        return list;
    }

    @PreDestroy
    public void close() {
        logger.info("close influxDB Factory!");
        for (Map.Entry<Integer, List<Influx>> entry : influxHolder.entrySet()) {
            for (Influx influx : entry.getValue()) {
                influx.close();
            }
        }
    }
}
