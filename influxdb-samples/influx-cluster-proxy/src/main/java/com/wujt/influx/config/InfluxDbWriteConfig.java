package com.wujt.influx.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "extend.influx.write")
@ConditionalOnProperty(prefix = "wujt.influxdb.proxy", value = "enable", havingValue = "true", matchIfMissing = true)
public class InfluxDbWriteConfig {
    private int action = 5000;

    private int bufferLimit = 10_000;

    private int flushDuration = 500; //ms

    private int jitterDuration = 100; //ms

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getBufferLimit() {
        return bufferLimit;
    }

    public void setBufferLimit(int bufferLimit) {
        this.bufferLimit = bufferLimit;
    }

    public int getFlushDuration() {
        return flushDuration;
    }

    public void setFlushDuration(int flushDuration) {
        this.flushDuration = flushDuration;
    }

    public int getJitterDuration() {
        return jitterDuration;
    }

    public void setJitterDuration(int jitterDuration) {
        this.jitterDuration = jitterDuration;
    }
}
