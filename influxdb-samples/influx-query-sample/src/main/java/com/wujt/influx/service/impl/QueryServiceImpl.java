package com.wujt.influx.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.wujt.influx.Influx;
import com.wujt.influx.cluster.InfluxFactory;
import com.wujt.influx.domin.ServerCountInfo;
import com.wujt.influx.domin.ServerInfo;
import com.wujt.influx.domin.schema.ServerInfoSchema;
import com.wujt.influx.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;

/**
 * link https://github.com/influxdata/influxdb-java/blob/master/src/test/java/org/influxdb/querybuilder/api/BuiltQueryTest.java
 *
 * @author wujt
 */
@Service
@Slf4j
public class QueryServiceImpl implements QueryService {

    private static final InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
    private final String DATABASE = "wujt_data_normal";
    private final String MEASUREMENT = "server_info";
    private final Integer BUCKET = 1;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    private InfluxFactory influxFactory;

    @PostConstruct
    public void init() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @PreDestroy
    private void destroy() {
        scheduledThreadPoolExecutor.shutdown();
    }

    @Override
    public Object query() {
        Query selectQuery = select().all().from(DATABASE, MEASUREMENT).orderBy(asc()).limit(10);
        log.info("query command {}", selectQuery.getCommand());

        Influx influx = influxFactory.getAvailableInfluxByBucket(BUCKET);
        return Optional.ofNullable(influx).map(client -> {

            QueryResult queryResult = client.read(selectQuery);

            List<ServerInfo> serverInfos = resultMapper.toPOJO(queryResult, ServerInfo.class);
            return serverInfos;
        }).orElse(null);

    }

    @Override
    public Object pageQuery(Integer page, Integer pageSize) {

        Query countQuery = select().countAll().from(DATABASE, MEASUREMENT)
                .where(eq(ServerInfoSchema.TAG_IP, "127.0.0.1")).and(gt(ServerInfoSchema.TAG_CPUS, "0"));

        Query pageQuery = select().all().from(DATABASE, MEASUREMENT)
                .where(eq(ServerInfoSchema.TAG_IP, "127.0.0.1")).and(gt(ServerInfoSchema.TAG_CPUS, "0"))
                .orderBy(desc()).limit(page, pageSize);
        log.info("query count command {}", countQuery.getCommand());
        log.info("query page command {}", pageQuery.getCommand());
        Influx influx = influxFactory.getAvailableInfluxByBucket(BUCKET);
        return Optional.ofNullable(influx).map(client -> {

            QueryResult queryResult = client.read(countQuery);
            List<ServerCountInfo> countInfos = resultMapper.toPOJO(queryResult, ServerCountInfo.class);

            QueryResult pageResult = client.read(pageQuery);
            List<ServerInfo> serverInfos = resultMapper.toPOJO(pageResult, ServerInfo.class);
            //数据组装返回
            long cnt = countInfos.isEmpty() ? 0 : countInfos.get(0).getCount();
            Page<ServerInfo> pageSetting = new Page<>(page, pageSize);
            pageSetting.setTotal(cnt);
            PageInfo<ServerInfo> pageInfo = new PageInfo<>(pageSetting);
            pageInfo.setList(serverInfos);
            pageInfo.setSize(serverInfos.size());
            return pageInfo;
        }).orElse(null);

    }
}
