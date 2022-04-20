package com.wujt.zookeeper.test;

import com.wujt.zookeeper.config.Config;
import com.wujt.zookeeper.config.EventDescription;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/6/25
 */
@Component
@Slf4j
public class SpeedTestComponent implements Watcher {

    Config config;
    protected String connectString;
    protected boolean connected = false;
    protected ZooKeeper zk;
    protected String rootNode;
    protected StringBuilder rootString = new StringBuilder();
    protected CountDownLatch latch, overarchingLatch;
    protected int writeCount = 0;

    @Autowired
    public SpeedTestComponent(Config config) {
        this.config = config;
        rootNode = config.getNodeRoot();
        rootString.append(config.getNodeRoot());
        this.config.loadConfig();
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getState()) {
            case SyncConnected:
                connected = true;
                break;
            case Disconnected:
            case Expired:
                connected = false;
                log.info("Not connected");
            default:
                log.info("Received event: " + event.getState());
        }
    }

    /**
     * 创建zookeeper client ，并进行链路watcher
     *
     * @return
     * @throws IOException
     */
    public ZooKeeper initAndWatcher()
            throws IOException {

        this.zk = new ZooKeeper(config.getConnectString(), 10000, this);

        return zk;
    }

    public List<String> getChildren() throws InterruptedException, KeeperException {
        return zk.getChildren(rootNode, false);
    }

    public void runWrite() {
        populate();
    }

    void populate() {
        overarchingLatch = new CountDownLatch(1);
        // 监听节点的创建
        zk.create("/test-",
                new byte[0],
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL,
                rootCb,
                null);
    }

    long lastWrite;
    AsyncCallback.StringCallback rootCb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  String name) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    // rootString 已经创建
                    rootString.append(name);
                    lastWrite = System.currentTimeMillis();

                    new Thread() {
                        public void run() {
                            long begin;
                            EventDescription event = config.nextEvent();
                            while (event != null) {
                                begin = System.currentTimeMillis();
                                latch = new CountDownLatch(event.delimiter);

                                StringBuilder rootBuilder = new StringBuilder();
                                byte[] data = new byte[event.size];
                                int j = 0;
                                String base=rootString.toString();

                                for (int i = 0; i < event.delimiter; i++) {
                                    if (!connected) {
                                        log.info("Stop sending requests, not connected");
                                        return;
                                    }

                                    if ((i % 10000) == 0) {
                                        base = rootBuilder.toString() + j;
                                        zk.create(base,
                                                data,
                                                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                                CreateMode.PERSISTENT,
                                                nullCb,
                                                null);
                                        j++;
                                    }
                                    zk.create(base + "/data-",
                                            data,
                                            ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                            CreateMode.PERSISTENT_SEQUENTIAL,
                                            stringCb,
                                            null);
                                }

                                /*
                                 * Waits for all responses before starting the
                                 * next stage.
                                 */
                                try {
                                    latch.await(event.delimiter * 15, TimeUnit.MILLISECONDS);
                                } catch (InterruptedException e) {
                                    log.info("Interrupted while waiting for stage to finish");
                                }

                                long end = System.currentTimeMillis();
                                long diff = end - begin;
                                log.info("Write time: " + diff + ", " + event.delimiter);
                                writeCount += event.delimiter;

                                event = config.nextEvent();
                            }

                            overarchingLatch.countDown();
                        }
                    }.start();

                    break;
                default:
                    log.info("ZK operation went wrong" + path);
            }
        }
    };

    AsyncCallback.StringCallback nullCb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  String name) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    break;
                default:
                    break;
            }
        }
    };

    AsyncCallback.StringCallback stringCb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  String name) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    latch.countDown();
                    if ((latch.getCount() % config.getSamples()) == 0) {
                        long current = System.currentTimeMillis();
                        long time = current - lastWrite;
                        log.info("Writes " + latch.getCount() + ": " + time);
                        lastWrite = current;
                    }

                    break;
                default:
                    log.info("Error when processing callback: " + path + ", " + rc);
                    break;
            }
        }
    };

    public void runRead() {
        overarchingLatch = new CountDownLatch(writeCount);
        zk.getChildren(rootString.toString(),
                false,
                childrenCb,
                null);
    }

    AsyncCallback.ChildrenCallback childrenCb = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  final List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    new Thread() {
                        public void run() {
                            for (String child : children) {
                                zk.getChildren(rootString + "/" + child,
                                        null,
                                        parentCb,
                                        null);
                            }
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    volatile int counter = 0;
    AsyncCallback.ChildrenCallback parentCb = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc,
                                  final String path,
                                  Object ctx,
                                  final List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    new Thread() {
                        public void run() {
                            for (String child : children) {
                                zk.getData(path + "/" + child,
                                        null,
                                        dataCb,
                                        null);
                            }
                        }
                    }.start();
                    break;
                default:
                    log.info("Something has gone wrong: " + path + ", " + rc);
            }
        }
    };

    AsyncCallback.DataCallback dataCb = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  byte[] data,
                                  Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    if (counter++ % 10000 == 0) {
                        log.info(path);
                    }
                    overarchingLatch.countDown();
                    break;
                default:
                    log.info("Something got wrong: " + path + ", " + rc);
            }
        }
    };

    public boolean await(long timeout)
            throws InterruptedException {
        return overarchingLatch.await(timeout, TimeUnit.MILLISECONDS);
    }

    public void cleanUp() {
        if (config.needsCleanup()) {
            return;
        }

        try {
            StringBuilder rootBuilder = new StringBuilder();
            rootBuilder.append(rootString + "/");
            List<String> children = zk.getChildren(rootString.toString(), false);
            for (String name : children) {
                List<String> subChildren = zk.getChildren(rootString.toString() + "/" + name, false);
                for (String child : subChildren) {
                    zk.delete(rootBuilder.toString() + name + "/" + child, -1, delCb, null);
                }
                zk.delete(rootBuilder.toString() + name, -1, delCb, null);
            }
            zk.delete(rootString.toString(), -1, delCb, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AsyncCallback.VoidCallback delCb = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    break;
                default:
                    log.info("Something went wrong: " + path + ", " + rc);
            }
        }
    };

    /*
     * Setup and run barrier
     */
    CountDownLatch barrier = new CountDownLatch(1);

    public void setUpBarrier()
            throws InterruptedException {
        zk.create("/barrier",
                new byte[0],
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                barrierParentCb,
                null);
        barrier.await();
        log.info("Crossed the barrier");
    }

    AsyncCallback.StringCallback barrierParentCb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  String name) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                case NODEEXISTS:
                    zk.create("/barrier/barrier-",
                            new byte[0],
                            ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.EPHEMERAL_SEQUENTIAL,
                            barrierNodeCb,
                            null);
                    break;
                default:
                    log.info("Something went wrong: " + path + ", " + rc);
            }

            log.info("Creating barrier node");
        }
    };

    String myBarrier = null;
    AsyncCallback.StringCallback barrierNodeCb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  String name) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    myBarrier = name;
                    zk.getChildren("/barrier",
                            childrenWatcher,
                            barrierChildrenCb,
                            null);
                    break;
                default:
                    log.info("Something went wrong: " + path + ", " + rc);
            }

            log.info("Getting children");
        }
    };

    Watcher childrenWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                zk.getChildren("/barrier",
                        childrenWatcher,
                        barrierChildrenCb,
                        null);
            }
        }
    };

    AsyncCallback.ChildrenCallback barrierChildrenCb = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case OK:
                    if (children.size() == config.getClients()) {
                        barrier.countDown();
                    }

                    break;
                default:
                    log.info("Something went wrong: " + path + ", " + rc);
            }
        }
    };

    public void releaseBarrier()
            throws KeeperException, InterruptedException {
        zk.delete(myBarrier, -1);
        List<String> children = zk.getChildren("/barrier", false);
        if (children.size() == 0) {
            zk.delete("/barrier", -1);
        }
    }

    public void close()
            throws InterruptedException {
        zk.close();
    }

    static void printHelp() {
        log.info("Use: java -cp ... SpeedTest \\ ");
        log.info("\t -Dconnect=<connect string>");
        log.info("\t -Dwritest=<numer of znodes>");
        log.info("\t -Dsize=<znode size>");
        log.info("\t -Dclients=<number of clients>");
        log.info("\t -Dcleanup=[true|false]");
    }

}
