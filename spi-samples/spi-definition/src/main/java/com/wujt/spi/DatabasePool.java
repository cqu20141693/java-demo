package com.wujt.spi;


import com.gow.spi.core.SPI;

@SPI("mongo")
public interface DatabasePool {

    void getConnection();

    void execute(String sql);

    void close();
}
