package com.wujt.server.model;

/**
 * 设备连接和断开事件枚举
 *
 *
 * @date 2017/7/12
 */
public enum ConnEventEnum {
    /**
     * some client is connect to this broker
     */
    CONNECT(1),
    /**
     * some client is disconnect from this broker
     */
    DISCONNECT(-1),
    /**
     * the client is continue online
     */
    ONLINE(0),
    // channel is writeable
    WRITEABLE(100);

    private final int value;

    ConnEventEnum(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public String stringValue() {
        switch (value) {
            case 1: {
                return "connect";
            }
            case -1: {
                return "disconnect";
            }
            case 0: {
                return "online";
            }
            case 100: {
                return "isWriteable";
            }
            default: {
                return "unknown";
            }
        }
    }

}
