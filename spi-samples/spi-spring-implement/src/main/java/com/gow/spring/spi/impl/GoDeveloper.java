package com.gow.spring.spi.impl;

import com.wujt.spi.Developer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author gow
 * @date 2021/6/23
 */
@Slf4j
public class GoDeveloper implements Developer, Ordered {
    @Override
    public void introduction() {
        log.info(" go develop");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
