package com.wujt;

import com.wujt.zookeeper.config.Config;
import com.wujt.zookeeper.test.SpeedTestComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author wujt
 */
@SpringBootApplication
@Slf4j
public class ZKApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ZKApp.class, args);
    }

    @Autowired
    private Config config;

    @Autowired
    private SpeedTestComponent test;

    @Override
    public void run(String... args) throws IOException, InterruptedException, KeeperException {

        log.info("/n/n/n");
        simpleTest();

        callbackTest();

        watherTest();
       // speedTest();
    }

    private void watherTest() {
        // todo
        //  ZooKeeper(String connectString, int sessionTimeout, Watcher watcher)
        //  register(Watcher watcher)
        //
        // List<String> getChildren(String path, Watcher watcher)
        // exists(String path, Watcher watcher)
        //getData(String path, Watcher watcher, Stat stat)
        //

    }

    private void callbackTest() {


        // todo node crete : StringCallback
        // todo node data changed watcher:
        // to node delete : VoidCallback
        // children changed
    }

    private void speedTest() throws IOException, InterruptedException, KeeperException {

        try {
            ZooKeeper zk = test.initAndWatcher();

            // add barrier node PERSISTENT and set StringCallback
            test.setUpBarrier();

            // 判断是否连接完成
            while (!test.isConnected()) {
                Thread.sleep(100);
            }

            log.info("Initializing");

            // 开始写节点数据 test
            test.runWrite();
            // wait MILLISECONDS
            boolean finished = test.await(100000);
            if (!finished) {
                log.info("Didn't finish writing");
            }

            log.info("Reading");
            long begin = System.currentTimeMillis();

            // 开始读取数据 读取子节点数据并 CountDownLatch
            test.runRead();
            test.await(100000);
            long end = System.currentTimeMillis();
            long diff = end - begin;

            log.info("Total time: " + diff);
            log.info("Before cleanup: " + test.getChildren());
            test.cleanUp();
            // Should contain a single item, zookeeper
            log.info("After cleanup: " + test.getChildren());

            test.releaseBarrier();

            log.info("Done");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (test != null) {
                try {
                    test.close();
                } catch (InterruptedException e) {
                    log.info("Interrupted while closing");
                }
            }
        }

    }

    private void simpleTest() throws IOException, InterruptedException, KeeperException {
        log.info("simple zookeeper test start");
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper =
                new ZooKeeper(config.getConnectString(),
                        4000, event -> {
                    if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到了服务端的响应事件，连接成功
                        countDownLatch.countDown();
                    }
                });
        countDownLatch.await();
        //CONNECTED
        log.info("zookeeper state:" + zooKeeper.getState());
        String path = "/runoob";
        Stat exists = zooKeeper.exists(path, false);
        if (exists != null) {
            byte[] data = zooKeeper.getData(path, false, exists);
            log.info("exist data=" + new String(data));
        } else {
            zooKeeper.create(path, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            log.info("add data=" + new String(zooKeeper.getData(path, false, null)));
        }
        zooKeeper.close();
        log.info("simple zookeeper test end");
    }
}
