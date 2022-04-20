package com.gow.spi.impl;

import com.gow.spi.core.Join;
import com.wujt.spi.DatabasePool;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Join
public class MongoDBPool implements DatabasePool {
    @Override
    public void getConnection() {
        log.info("MongoDB connect established  ");
    }

    @Override
    public void execute(String sql) {
        log.info("MongoDB sql={} execute ", sql);
    }

    @Override
    public void close() {
        log.info("MongoDB connect closed  ");
    }
}
