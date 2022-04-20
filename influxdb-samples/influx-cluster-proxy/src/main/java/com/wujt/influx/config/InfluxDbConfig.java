package com.wujt.influx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * 由dataId : influx-proxy-core.yml定义配置
 * 使用的系统需引入
 */
@Configuration
@ConfigurationProperties(prefix = "extend.influx")
@RefreshScope
@ConditionalOnProperty(prefix = "wujt.influxdb.proxy", value = "enable", havingValue = "true", matchIfMissing = true)
public class InfluxDbConfig {
    private final static Logger logger = LoggerFactory.getLogger(InfluxDbConfig.class);

    /**
     * influxDb服务器地址及bucket号,逗号隔开
     * 如: localhost:8086:1,127.0.0.1:8086:1,127.0.0.2:8086:2,127.0.0.3:8086:2
     * bucket由1号开始编号
     */
    private String serversAndBucket;

    /**
     * 用户名
     */
    private String user = "root";

    /**
     * 密码
     */
    private String password = "root";

    /**
     * 默认retention策略名，需要是db关联的默认策略
     */
    private String defaultDataRetentionPolicy = "default_normal";

    /**
     * 默认db名
     */
    private String defaultDataDbName = "wujt_data_normal";

    /**
     * 默认retention策略名，需要是db关联的默认策略
     */
    private String defaultEventInfoRetentionPolicy = "default_normal";

    /**
     * 默认db名
     */
    private String defaultEventInfoDbName = "wujt_data_normal";

    /**
     * 默认retention策略名，需要是db关联的默认策略
     */
    private String defaultSysLogRetentionPolicy = "default_normal";

    /**
     * 默认db名
     */
    private String defaultSysLogDbName = "wujt_sys_info_normal";

    /**
     * 默认retention策略名，需要是db关联的默认策略
     */
    private String defaultCmdRetentionPolicy = "default_normal";

    /**
     * 默认db名
     */
    private String defaultCmdDbName = "wujt_cmd_normal";

    /**
     * 本地存储地址
     */
    private String localStorePath = "/data/wujt";

    /**
     * 是否开启正常写入时的旁路写
     */
    private Boolean startByPassWrite = false;

    public String getServersAndBucket() {
        Assert.notNull(serversAndBucket, "serversAndBucket为空。");
        return serversAndBucket;
    }

    public void setServersAndBucket(String serversAndBucket) {
        Assert.notNull(serversAndBucket, "设置serversAndBucket为空。");
        logger.info("update serversAndBucket config : {}", serversAndBucket);
        this.serversAndBucket = serversAndBucket;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocalStorePath() {
        return localStorePath;
    }

    public void setLocalStorePath(String localStorePath) {
        this.localStorePath = localStorePath;
    }

    public String getDefaultDataRetentionPolicy() {
        return defaultDataRetentionPolicy;
    }

    public void setDefaultDataRetentionPolicy(String defaultDataRetentionPolicy) {
        this.defaultDataRetentionPolicy = defaultDataRetentionPolicy;
    }

    public String getDefaultDataDbName() {
        return defaultDataDbName;
    }

    public void setDefaultDataDbName(String defaultDataDbName) {
        this.defaultDataDbName = defaultDataDbName;
    }

    public String getDefaultEventInfoRetentionPolicy() {
        return defaultEventInfoRetentionPolicy;
    }

    public void setDefaultEventInfoRetentionPolicy(String defaultEventInfoRetentionPolicy) {
        this.defaultEventInfoRetentionPolicy = defaultEventInfoRetentionPolicy;
    }

    public String getDefaultEventInfoDbName() {
        return defaultEventInfoDbName;
    }

    public void setDefaultEventInfoDbName(String defaultEventInfoDbName) {
        this.defaultEventInfoDbName = defaultEventInfoDbName;
    }

    public String getDefaultSysLogRetentionPolicy() {
        return defaultSysLogRetentionPolicy;
    }

    public void setDefaultSysLogRetentionPolicy(String defaultSysLogRetentionPolicy) {
        this.defaultSysLogRetentionPolicy = defaultSysLogRetentionPolicy;
    }

    public String getDefaultSysLogDbName() {
        return defaultSysLogDbName;
    }

    public void setDefaultSysLogDbName(String defaultSysLogDbName) {
        this.defaultSysLogDbName = defaultSysLogDbName;
    }

    public Boolean getStartByPassWrite() {
        return startByPassWrite;
    }

    public void setStartByPassWrite(Boolean startByPassWrite) {
        this.startByPassWrite = startByPassWrite;
    }

    public String getDefaultCmdRetentionPolicy() {
        return defaultCmdRetentionPolicy;
    }

    public void setDefaultCmdRetentionPolicy(String defaultCmdRetentionPolicy) {
        this.defaultCmdRetentionPolicy = defaultCmdRetentionPolicy;
    }

    public String getDefaultCmdDbName() {
        return defaultCmdDbName;
    }

    public void setDefaultCmdDbName(String defaultCmdDbName) {
        this.defaultCmdDbName = defaultCmdDbName;
    }
}
