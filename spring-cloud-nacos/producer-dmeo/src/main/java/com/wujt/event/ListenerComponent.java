package com.wujt.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wujt  2021/5/28
 */
@Component
@Slf4j
public class ListenerComponent {

    @EventListener
    public void  onApplicationEvent(MySelfEvent mySelfEvent){
        log.info("ListenerComponent receive event={},event source={}", mySelfEvent.getClass(), mySelfEvent.getSource().getClass());
    }
}
