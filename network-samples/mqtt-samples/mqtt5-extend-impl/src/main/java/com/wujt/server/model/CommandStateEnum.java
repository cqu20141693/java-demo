package com.wujt.server.model;

/**
 * Command status value
 *
 * @date 2017/7/12
 */
public enum CommandStateEnum {
    /**
     * 未知状态
     */
    UNKNOWN(0),
    /**
     * the command is saved but has not been send to the device
     */
    SAVED(1),
    /**
     * the command is send to the device and dons't receive the ack message
     */
    SENDED(2),
    /**
     * the command is send to the device and has receive the ack message
     */
    ACKED(3),
    /**
     * the command is send to the device, but don't receive the mqtt ack message before expire
     */
    EXPIRED(4),
    /**
     * the device publish cmdack/...  data
     */
    COMPLETE(5),
    /**
     * the command send to the device failed
     */
    FAILED(-1),
    /**
     * the command is canceled by the manager
     */
    CANCELED(-2);

    private final int value;

    CommandStateEnum(int value) {
        this.value = value;
    }

    public static CommandStateEnum fromIntValue(int value) {
        switch (value) {
            case 1: {
                return SAVED;
            }
            case 2: {
                return SENDED;
            }
            case 3: {
                return ACKED;
            }
            case 4: {
                return EXPIRED;
            }
            case 5: {
                return COMPLETE;
            }
            case -1: {
                return FAILED;
            }
            case -2: {
                return CANCELED;
            }
            default: {
                return UNKNOWN;
            }
        }
    }

    public int intValue() {
        return value;
    }

    public String stringValue() {
        switch (value) {
            case 1: {
                return "save";
            }
            case 2: {
                return "send";
            }
            case 3: {
                return "ack";
            }
            case 4: {
                return "expired";
            }
            case 5: {
                return "complete";
            }
            case -1: {
                return "fail";
            }
            case -2: {
                return "cancel";
            }
            default: {
                return "unknown";
            }
        }
    }
}
