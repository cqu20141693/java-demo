package com.wujt.influx.service.impl;

import com.wujt.RandomUtil;
import com.wujt.influx.cluster.InfluxCluster;
import com.wujt.influx.cluster.TargetType;
import com.wujt.influx.domin.ServerInfo;
import com.wujt.influx.domin.schema.ServerInfoSchema;
import com.wujt.influx.service.WriteService;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
@Service
public class WriteServiceImpl implements WriteService {

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Autowired
    private InfluxCluster influxCluster;

    @PostConstruct
    public void init() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void startInsert() {

        scheduledThreadPoolExecutor.scheduleAtFixedRate(this::sendData, 10, 10000, TimeUnit.MILLISECONDS);
        sendData();

    }

    private void sendData() {
        ServerInfo serverInfo = ServerInfo.builder().name("wujt-test").ip("127.0.0.1")
                .cpu(RandomUtil.getRandomNumer(4)).memory(RandomUtil.getRandomNumer(16)).bandwidth(100).thread(2)
                .currentProcesses(RandomUtil.getRandomNumer(100))
                .build();
        Point.Builder pointBuilder = Point.measurement(ServerInfoSchema.MEASUREMENT)
                .time(serverInfo.getTime() > 0 ? serverInfo.getTime() : System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag(ServerInfoSchema.TAG_IP, serverInfo.getIp())
                .tag(ServerInfoSchema.TAG_CPUS, String.valueOf(serverInfo.getCpu()))
                .tag(ServerInfoSchema.TAG_MEMORY, String.valueOf(serverInfo.getMemory()))
                .tag(ServerInfoSchema.TAG_THREAD, String.valueOf(serverInfo.getThread()))
                .tag(ServerInfoSchema.TAG_BANDWIDTH, String.valueOf(serverInfo.getBandwidth()))
                .addField(ServerInfoSchema.FILED_CURRENT_PROCESS, serverInfo.getCurrentProcesses())
                .addField(ServerInfoSchema.FILED_NAME, serverInfo.getName());

        influxCluster.writePoint(pointBuilder.build(), serverInfo.getCpu(), serverInfo.getMemory(), TargetType.data);
    }

    @PreDestroy
    private void destroy() {
        scheduledThreadPoolExecutor.shutdown();
    }
}
