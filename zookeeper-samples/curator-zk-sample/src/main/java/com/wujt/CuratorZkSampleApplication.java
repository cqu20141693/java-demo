package com.wujt;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CuratorZkSampleApplication implements CommandLineRunner {

    @Value("${spring.cloud.zookeeper.connect-string}")
    private String connectString;

    public static void main(String[] args) {

        SpringApplication.run(CuratorZkSampleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.
                builder().connectString(connectString).
                sessionTimeoutMs(4000).retryPolicy(new
                ExponentialBackoffRetry(1000, 3)).
                namespace("").build();
        curatorFramework.start();
        Stat stat = new Stat();
        //查询节点数据
        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/runoob");
        System.out.println(new String(bytes));
        curatorFramework.close();
    }
}
