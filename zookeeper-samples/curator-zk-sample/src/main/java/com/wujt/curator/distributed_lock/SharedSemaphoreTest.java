package com.wujt.curator.distributed_lock;

import com.wujt.curator.util.ZKUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wujt
 */
public class SharedSemaphoreTest {
    private static final int MAX_LEASE = 10;
    private static final String PATH = "/testZK/semaphore";
    private static final FakeLimitedResource resource = new FakeLimitedResource();

    public static void main(String[] args) throws Exception {
        CuratorFramework client = ZKUtils.getClient();
        client.start();
        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, PATH, MAX_LEASE);
        Collection<Lease> leases = semaphore.acquire(5);
        System.out.println("获取租约数量：" + leases.size());
        Lease lease = semaphore.acquire();
        System.out.println("获取单个租约");
        resource.use(); // 使用资源
        // 再次申请获取5个leases，此时leases数量只剩4个，不够，将超时
        Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
        System.out.println("获取租约，如果超时将为null： " + leases2);
        System.out.println("释放租约");
        semaphore.returnLease(lease);
        // 再次申请获取5个，这次刚好够
        leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
        System.out.println("获取租约，如果超时将为null： " + leases2);
        System.out.println("释放集合中的所有租约");
        semaphore.returnAll(leases);
        semaphore.returnAll(leases2);
        client.close();
        System.out.println("结束!");
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
