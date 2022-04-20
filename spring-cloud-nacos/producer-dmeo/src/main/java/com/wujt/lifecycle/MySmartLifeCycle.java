package com.wujt.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wujt  2021/6/2
 */
@Slf4j
@Component
public class MySmartLifeCycle implements SmartLifecycle {
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 默认true: 上下文启动后会执行
     *
     * @return boolean
     * @date 2021/6/2 15:13
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {

        this.stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            log.info("MySmartLifeCycle start method invoked");
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            log.info("MySmartLifeCycle stop method invoked");

        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }
}
