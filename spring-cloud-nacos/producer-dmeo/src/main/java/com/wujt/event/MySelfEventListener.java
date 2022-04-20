package com.wujt.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author wujt  2021/5/28
 */
@Slf4j
public class MySelfEventListener implements ApplicationListener<MySelfEvent>  {
    @Override
    public void onApplicationEvent(MySelfEvent mySelfEvent) {
        log.info("MySelfEventListener receive event={},event source={}", mySelfEvent.getClass(), mySelfEvent.getSource().getClass());
    }
}
