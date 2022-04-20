package com.wujt.curator.distributed_lock;

import com.wujt.curator.util.ZKUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wujt
 */
public class SharedReentrantLockTest {

    private static final String lockPath = "/testZK/sharedreentrantlock";
    private static final Integer clientNums = 5;
    final static FakeLimitedResource resource = new FakeLimitedResource(); // 共享的资源
    private static CountDownLatch countDownLatch = new CountDownLatch(clientNums);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < clientNums; i++) {
            String clientName = "client#" + i;
            // 创建五个并发线程
            new Thread(() -> {
                CuratorFramework client = ZKUtils.getClient();
                client.start();
                Random random = new Random();
                try {
                    // 创建可重入锁
                    final InterProcessMutex lock = new InterProcessMutex(client, lockPath);
                    // 每个客户端请求10次共享资源
                    for (int j = 0; j < 10; j++) {
                        //
                        if (!lock.acquire(10, TimeUnit.SECONDS)) {
                            throw new IllegalStateException(j + ". " + clientName + " 不能得到互斥锁");
                        }
                        try {
                            System.out.println(j + ". " + clientName + " 已获取到互斥锁");
                            resource.use(); // 使用资源
                            if (!lock.acquire(10, TimeUnit.SECONDS)) {
                                throw new IllegalStateException(j + ". " + clientName + " 不能再次得到互斥锁");
                            }
                            System.out.println(j + ". " + clientName + " 已再次获取到互斥锁");
                            lock.release(); // 申请几次锁就要释放几次锁
                        } finally {
                            System.out.println(j + ". " + clientName + " 释放互斥锁");
                            lock.release(); // 总是在finally中释放
                        }
                        Thread.sleep(random.nextInt(100));
                    }
                } catch (Throwable e) {
                    System.out.println(e.getMessage());
                } finally {
                    CloseableUtils.closeQuietly(client);
                    System.out.println(clientName + " 客户端关闭！");
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("结束！");
    }

    private static class FakeLimitedResource {
        private final AtomicBoolean inUse = new AtomicBoolean(false);

        // 模拟只能单线程操作的资源
        public void use() throws InterruptedException {
            if (!inUse.compareAndSet(false, true)) {
                // 在正确使用锁的情况下，此异常不可能抛出
                throw new IllegalStateException("Needs to be used by one client at a time");
            }
            try {
                Thread.sleep((long) (100 * Math.random()));
            } finally {
                inUse.set(false);
            }
        }
    }


}
