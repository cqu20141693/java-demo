package com.wujt.influx.cluster;


import com.wujt.influx.Influx;
import com.wujt.influx.InfluxDbProxyException;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;

import java.util.Map;

/**
 *
 */
public interface InfluxCluster {
    /**
     * 写数据点
     *
     * @param point
     * @param productId
     * @param deviceId
     */
    void writeData(Point point, long productId, long deviceId);

    /**
     * 写数据
     *
     * @param point
     * @param targetType 数据类型
     * @param productId
     * @param deviceId
     */
    void writePoint(Point point, long productId, long deviceId, TargetType targetType);

    /**
     * 仅旁路规则写数据
     *
     * @param point
     * @param targetType 数据类型
     * @param productId
     * @param deviceId
     */
    void byPassWritePoint(Point point, long productId, long deviceId, TargetType targetType);

    /**
     * 设备级别读数据点操作
     *
     * @param queryString
     * @param paramsHolder
     * @param productId
     * @param deviceId
     * @return
     * @throws InfluxDbProxyException
     */
    QueryResult readDataDeviceLevel(String queryString, Map<String, Object> paramsHolder, long productId, long deviceId) throws InfluxDbProxyException;


    /**
     * 设备级别读数据操作
     *
     * @param queryString
     * @param paramsHolder
     * @param productId
     * @param deviceId
     * @return
     * @throws InfluxDbProxyException
     */
    QueryResult readDeviceLevel(String queryString, Map<String, Object> paramsHolder, long productId, long deviceId, TargetType targetType) throws InfluxDbProxyException;


    /**
     * 产品级别读数据点操作
     *
     * @param queryString
     * @param paramsHolder
     * @param productId
     * @return
     * @throws InfluxDbProxyException
     */
    QueryResult readDataProductLevel(String queryString, Map<String, Object> paramsHolder, long productId) throws InfluxDbProxyException;

    /**
     * 产品级别读数据操作
     *
     * @param queryString
     * @param paramsHolder
     * @param productId
     * @return
     * @throws InfluxDbProxyException
     */
    QueryResult readProductLevel(String queryString, Map<String, Object> paramsHolder, long productId, TargetType targetType) throws InfluxDbProxyException;


    /**
     * 指定库读产品级别数据点数据
     *
     * @param queryString
     * @param paramsHolder
     * @param productId
     * @return
     * @throws InfluxDbProxyException
     */
    QueryResult readDataProductLevelSpecDb(String queryString, Map<String, Object> paramsHolder, long productId, String database) throws InfluxDbProxyException;
}
