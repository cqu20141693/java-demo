package com.wujt.curator.distributed_lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wujt
 */
public class LockImpl {
    @Autowired
    private CuratorFramework connect;

    public void getLock(String path) throws Exception {
        System.out.println(Thread.currentThread().getName() + "已连接，状态：" + connect.getState());
        connect.checkExists().creatingParentsIfNeeded().forPath(path);
        // 可重入锁
        InterProcessMutex interProcessMutex = new InterProcessMutex(connect, path);
        interProcessMutex.acquire();
        System.out.println(Thread.currentThread().getName() + "获得锁");
        System.out.println("do somethings");
        interProcessMutex.release();
        System.out.println(Thread.currentThread().getName() + "释放锁");
    }
}
