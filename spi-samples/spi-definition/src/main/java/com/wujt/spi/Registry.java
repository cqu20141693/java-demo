package com.wujt.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author gow
 * @date 2021/6/27 0027
 */
@SPI("zookeeper")
public interface Registry {
    /**
     * 注册服务
     */
    @Adaptive()
    String register(URL url, String content);

    /**
     * 发现服务
     */
    @Adaptive()
    String discovery(URL url, String content);
}
