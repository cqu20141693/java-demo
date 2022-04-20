package com.gow.spi.impl;

import com.gow.spi.core.Join;
import com.wujt.spi.DatabasePool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Join
public class RedisPool implements DatabasePool {
    @Override
    public void getConnection() {
        log.info("Redis connect established  ");
    }

    @Override
    public void execute(String command) {
        log.info("Redis command={} execute ", command);
    }

    @Override
    public void close() {
        log.info("Redis connect closed  ");
    }
}
