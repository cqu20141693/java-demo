package com.wujt.curator.distributed_lock;

import com.wujt.curator.util.ZKUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wujt
 */
public class InterProcessReadWriteLockTest {

    private static final String lockPath = "/testZK/sharedreentrantlock";
    private static final Integer clientNums = 5;
    final static FakeLimitedResource resource = new FakeLimitedResource(); // 共享的资源
    private static CountDownLatch countDownLatch = new CountDownLatch(clientNums);
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < clientNums; i++) {
            final String clientName = "client#" + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CuratorFramework client = ZKUtils.getClient();
                    client.start();
                    final InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, lockPath);
                    final InterProcessMutex readLock = lock.readLock();
                    final InterProcessMutex writeLock = lock.writeLock();

                    try {
                        // 注意只能先得到写锁再得到读锁，不能反过来！！！
                        if (!writeLock.acquire(10, TimeUnit.SECONDS)) {
                            throw new IllegalStateException(clientName + " 不能得到写锁");
                        }
                        System.out.println(clientName + " 已得到写锁");
                        if (!readLock.acquire(10, TimeUnit.SECONDS)) {
                            throw new IllegalStateException(clientName + " 不能得到读锁");
                        }
                        System.out.println(clientName + " 已得到读锁");
                        try {
                            resource.use(); // 使用资源
                        } finally {
                            System.out.println(clientName + " 释放读写锁");
                            readLock.release();
                            writeLock.release();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        CloseableUtils.closeQuietly(client);
                        countDownLatch.countDown();
                    }
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
