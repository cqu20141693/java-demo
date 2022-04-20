package com.gow.influxdb;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.desc;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.select;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@Component
@Slf4j
public class InfluxOperation {
    private static final InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
    private final String DATABASE = "ts_data";
    private final String MEASUREMENT = "data";

    @Autowired
    private InfluxDB influxDb;

    @PostConstruct
    public void test() {

        int page = 1;
        int pageSize = 10;
        Query countQuery = select().countAll().from(DATABASE, MEASUREMENT);

        Query pageQuery = select().all().from(DATABASE, MEASUREMENT)
                .orderBy(desc()).limit(page, pageSize);
        log.info("query count command {}", countQuery.getCommand());
        log.info("query page command {}", pageQuery.getCommand());
        QueryResult query = influxDb.query(countQuery);
        QueryResult query1 = influxDb.query(pageQuery);
        log.info("count={}", query);
        log.info("pageInfos={}", query1);
    }

}
