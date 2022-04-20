package com.wujt.influx.cluster;


import com.wujt.influx.Influx;
import com.wujt.influx.InfluxDbProxyException;
import com.wujt.influx.InfluxReadOperation;
import com.wujt.influx.InfluxWriteOperation;
import com.wujt.influx.cluster.sharding.ShardingParam;
import com.wujt.influx.config.InfluxDbConfig;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 *
 */
@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class InfluxClusterImpl implements InfluxCluster {
    private static Logger logger = LoggerFactory.getLogger(InfluxClusterImpl.class);

    @Autowired
    private InfluxFactory influxFactory;

    @Autowired
    private InfluxDbConfig influxDbConfig;

    /**
     * influxdb集群写数据点操作
     */
    @Override
    public void writeData(Point point, long productId, long deviceId) {
        writePoint(point, productId, deviceId, TargetType.data);
    }

    @Override
    public void writePoint(Point point, long productId, long deviceId, TargetType targetType) {
        ShardingParam shardingParam = ShardingParam.create(productId, deviceId, targetType);
        InfluxWriteOperation influxWriteOperation = influxFactory.fetchWriteInflux(shardingParam);
        if (influxWriteOperation.getInfluxList() != null) {
            for (Influx influx : influxWriteOperation.getInfluxList()) {
                //单写都是入队列操作，速度快
                influx.write(influxWriteOperation.getDatabase(), influxWriteOperation.getRetentionPolicy(), point);
            }
        }
        //是否旁路写入
        if(influxDbConfig.getStartByPassWrite()){
            //旁路写入，写入迁入的库，数据迁移时有用
            InfluxWriteOperation byPassInfluxWriteOperation = influxFactory.fetchByPassWriteInflux(shardingParam);
            if (byPassInfluxWriteOperation.getInfluxList() != null) {
                for (Influx influx : byPassInfluxWriteOperation.getInfluxList()) {
                    influx.write(byPassInfluxWriteOperation.getDatabase(), byPassInfluxWriteOperation.getRetentionPolicy(), point);
                }
            }
        }
    }

    @Override
    public void byPassWritePoint(Point point, long productId, long deviceId, TargetType targetType) {
        //旁路写入，数据迁移时有用
        InfluxWriteOperation byPassInfluxWriteOperation = influxFactory.fetchByPassWriteInflux(ShardingParam.create(productId, deviceId, targetType));
        if (byPassInfluxWriteOperation.getInfluxList() != null) {
            for (Influx influx : byPassInfluxWriteOperation.getInfluxList()) {
                influx.write(byPassInfluxWriteOperation.getDatabase(), byPassInfluxWriteOperation.getRetentionPolicy(), point);
            }
        }
    }

    @Override
    public QueryResult readDataDeviceLevel(String queryString, Map<String, Object> paramsHolder, long productId, long deviceId) throws InfluxDbProxyException {
        return readDeviceLevel(queryString, paramsHolder, productId, deviceId, TargetType.data);
    }

    @Override
    public QueryResult readDeviceLevel(String queryString, Map<String, Object> paramsHolder, long productId, long deviceId, TargetType targetType) throws InfluxDbProxyException {
        InfluxReadOperation influxReadOperation = influxFactory.fetchReadInfluxByDeviceId(ShardingParam.create(productId, deviceId, targetType));
        Influx influx = influxReadOperation.getInflux();
        if (influx == null || !influx.isAlive()) {
            logger.error("influxdb异常,没有对应的健康节点，deviceId = {}", deviceId);
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        QueryResult queryResult = influx.read(queryString, paramsHolder, influxReadOperation.getDatabase());
        if (queryResult.hasError()) {
            logger.error("influxdb读取异常:" + queryResult.getError());
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        return queryResult;
    }

    @Override
    public QueryResult readDataProductLevel(String queryString, Map<String, Object> paramsHolder, long productId) throws InfluxDbProxyException {
        return readProductLevel(queryString, paramsHolder, productId, TargetType.data);
    }

    @Override
    public QueryResult readProductLevel(String queryString, Map<String, Object> paramsHolder,
                                        long productId, TargetType targetType) throws InfluxDbProxyException {
        InfluxReadOperation influxReadOperation = influxFactory.fetchReadInfluxByProductId(ShardingParam.create()
                .setProductId(productId).setTargetType(targetType));
        Influx influx = influxReadOperation.getInflux();
        if (influx == null || !influx.isAlive()) {
            logger.error("influxdb异常,没有对应的健康节点，productId = {} ", productId);
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        QueryResult queryResult = influx.read(queryString, paramsHolder, influxReadOperation.getDatabase());
        if (queryResult.hasError()) {
            logger.error("influxdb读取异常:" + queryResult.getError());
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        return queryResult;
    }

    @Override
    public QueryResult readDataProductLevelSpecDb(String queryString, Map<String, Object> paramsHolder, long productId, String database) throws InfluxDbProxyException {
        if (StringUtils.isEmpty(database)) {
            logger.error("指定database不可为空");
            throw new InfluxDbProxyException("influxdb操作异常，指定database不可为空");
        }
        InfluxReadOperation influxReadOperation = influxFactory.fetchSpecReadInflux(ShardingParam.create().setProductId(productId), null);
        Influx influx = influxReadOperation.getInflux();
        if (influx == null || !influx.isAlive()) {
            logger.error("influxdb异常,没有对应的健康节点，productId = {}", productId);
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        QueryResult queryResult = influx.read(queryString, paramsHolder, database);
        if (queryResult.hasError()) {
            logger.error("influxdb读取异常: {}", queryResult.getError());
            throw new InfluxDbProxyException("influxdb操作异常");
        }
        return queryResult;
    }

    /**
     * 周期性的对集群内的节点健康进行检查
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void clusterHealthCheck() {
        for (InfluxReadOperation influxReadOperation : influxFactory.fetchAllInflux()) {
            if (influxReadOperation.getInflux() != null) {
                influxReadOperation.getInflux().healthCheck();
            }
        }
    }
}
