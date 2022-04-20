package com.wujt.udp;

/**
 * @author wujt
 */
public class MessageBean {
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "time='" + time + '\'' +
                '}';
    }
}
