package com.wujt.influx;

/**
 * @author zc
 * @date 2019/7/15 11:41
 */
public class InfluxDbProxyException extends Exception {
    public InfluxDbProxyException(String message) {
        super(message);
    }

    public InfluxDbProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfluxDbProxyException(Throwable cause) {
        super(cause);
    }
}
