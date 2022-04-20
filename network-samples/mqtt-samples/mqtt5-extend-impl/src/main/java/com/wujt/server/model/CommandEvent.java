package com.wujt.server.model;

/**
 * mqtt broker 写入kafka中的命令事件的数据结构
 *
 *
 * @date 2018/3/30
 */
public class CommandEvent {
    /**
     * 命令下发目标所属的 userId
     */
    private int userId;
    /**
     * 命令下发目标所属的 productId
     */
    private int productId;
    /**
     * 命令下发目标对应的 deviceId
     */
    private int deviceId;
    /**
     * 命令下发目标设备所属的userKey
     */
    private String userKey;
    /**
     * 命令下发目标设备所属的productKey
     */
    private String productKey;
    /**
     * 命令下发目标的deviceKey
     */
    private String deviceKey;
    /**
     * 命令Id，每个命令都有一个独一无二的命令Id
     */
    private String cmdId;
    /**
     * 命令的状态，具体编码参见 {@link CommandStateEnum}
     */
    private int state;
    /**
     * 命令状态发生的时间
     */
    private Long time;
    /**
     * 命令到期时间
     */
    private Long timeDue;
    /**
     * 命令的响应，为字节数组
     */
    private byte[] output;
    /**
     * 命令模板名称
     */
    private String template;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTimeDue() {
        return timeDue;
    }

    public void setTimeDue(Long timeDue) {
        this.timeDue = timeDue;
    }

    public byte[] getOutput() {
        return output;
    }

    public void setOutput(byte[] output) {
        this.output = output;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
