package com.wujt.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author wujt  2021/5/28
 */
public class MySelfEvent extends ApplicationEvent {
    /**
     * @param source 事件发送方
     * @return null
     * @date 2021/5/28 11:13
     */
    public MySelfEvent(Object source) {
        super(source);
    }
}
