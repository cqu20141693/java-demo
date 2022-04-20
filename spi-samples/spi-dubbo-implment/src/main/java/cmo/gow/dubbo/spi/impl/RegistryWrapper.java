package cmo.gow.dubbo.spi.impl;

import com.wujt.spi.Registry;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.context.Lifecycle;
import org.springframework.util.StopWatch;

/**
 * @author gow
 * @date 2021/6/27 0027
 */
@Slf4j
public class RegistryWrapper implements Registry, Lifecycle {
    // 持有 扩展点接口
    private Registry registry;

    // 构造器注入
    public RegistryWrapper(Registry registry) {
        this.registry = registry;
    }

    @Override
    public String register(URL url, String content) {
        preHandle(url, content);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String register = registry.register(url, content);
        stopWatch.stop();
        log.info("registry use time={}", stopWatch.getLastTaskTimeNanos());
        postHandle(url, content, register);
        return register;
    }

    @Override
    public String discovery(URL url, String content) {
        preHandle(url, content);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String discovery = registry.discovery(url, content);
        stopWatch.stop();
        log.info("discovery use time={}", stopWatch.getLastTaskTimeNanos());
        postHandle(url, content, discovery);
        return discovery;
    }


    private void postHandle(URL url, String content, String register) {
        log.info("post handle invoked");
    }

    private void preHandle(URL url, String content) {
        log.info("pre handle invoked ");
    }


    @Override
    public void initialize() throws IllegalStateException {
        log.info("initialize is invoked");
    }

    @Override
    public void start() throws IllegalStateException {
        log.info("start is invoked");
    }

    @Override
    public void destroy() throws IllegalStateException {
        log.info("destroy is invoked");
    }
}
