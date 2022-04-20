package com.gow.spi.impl;

import com.wujt.spi.Developer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt
 */
@Slf4j
public class JavaDeveloper implements Developer {
    @Override
    public void introduction() {
        log.info("I am a Java Developer, I can do everything.");
    }
}
