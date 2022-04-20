package com.cc.core.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoJson;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;

import static com.cc.core.service.impl.DeviceServiceImpl.DEVICE_INDEX;

/**
 * 使用场景：
 * 分布式搜索引擎： 查询，分词搜索，标签搜索
 * 数据分析：聚合
 * 地理位置搜索
 * 日志存储和分析，时序数据存储分析
 * mysql 大表同步到es，es事务支持比较弱
 * <p>
 * <p>
 * <p>
 * 工具：
 * kibana: 可视化分析和管理
 * elasticsearch : 存储和索引
 * Logstash(插件，转化，聚合),beats(不同的数据源) : 数据抓取
 * X-pack：机器学习和告警监控 ，开源商业包
 * <p>
 * <p>
 * <p>
 *
 * @TypeAlias，_class字段别名 WriteTypeHint.FALSE : 禁止_class 字段，官方建议不禁用，用于多态对象
 */
@Document(indexName = DEVICE_INDEX, writeTypeHint = WriteTypeHint.FALSE)
@Setting(shards = 2, replicas = 2, refreshInterval = "10s")
@Data
public class DeviceGeo {
    /**
     * 其和传统的数据库主键的作用是一样的，默认的话ES会在后端自动生产一个UUID，当然也可以自己赋值去覆盖。
     */
    @Id
    private String id;
    /**
     * 根据设备id精确搜索
     */
    @Field(name = "deviceId", type = FieldType.Keyword)
    private String deviceId;

    /**
     * 根据设备名称模糊搜索，利用分词
     */
    @Field(name = "name", type = FieldType.Text)
    private String name;

    /**
     * 根据创建时间进行聚合统计查询
     */
    @Field(name = "createTime", type = FieldType.Date)
    private Date createTime;

    /**
     * 地理坐标点（geo-point）是指地球表面可以用经纬度描述的一个点。
     * 地理坐标点可以用来计算两个坐标位置间的距离，或者判断一个点是否在一个区域中。
     */
    @GeoPointField
    private Point location;

    /**
     * 设备形状
     * https://blog.csdn.net/crazyo2jam/article/details/118933973
     */
    @GeoShapeField
    private GeoJson shape;
    /**
     * 数组使用Text类型
     * https://www.elastic.co/guide/en/elasticsearch/reference/7.17/array.html
     */
    @Field(name = "tags", type = FieldType.Text)
    private List<String> tags;

    /**
     * 堆栈日志
     * 可用于like， 正则匹配搜素
     * https://www.elastic.co/guide/en/elasticsearch/reference/7.17/keyword.html#wildcard-field-type
     */
    @Field(name = "stackLog", type = FieldType.Wildcard)
    private String stackLog;
}
