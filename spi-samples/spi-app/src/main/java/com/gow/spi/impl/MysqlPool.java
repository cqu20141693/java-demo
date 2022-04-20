package com.gow.spi.impl;

import com.gow.spi.core.Join;
import com.wujt.spi.DatabasePool;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Join
public class MysqlPool implements DatabasePool {
    @Override
    public void getConnection() {
        log.info("Mysql connect established  ");
    }

    @Override
    public void execute(String sql) {
        log.info("Mysql sql={} execute ", sql);
    }

    @Override
    public void close() {
        log.info("Mysql connect closed  ");
    }
}
