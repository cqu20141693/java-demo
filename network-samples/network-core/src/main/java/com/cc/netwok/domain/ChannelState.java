package com.cc.netwok.domain;

/**
 * channel 状态，用于行为判断
 * wcc 2022/5/14
 */
public enum ChannelState {
    CONNECT_ACTIVE(1, "connect active"),
    AUTHED(1 << 2, "auth"),
    INACTIVE(1 << 3, "auth"),
    ;

    ChannelState(int state, String text) {
        this.state = state;
        this.text = text;
    }

    public int getState() {
        return state;
    }

    private int state;
    private String text;
}
