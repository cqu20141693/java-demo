package com.wujt.并发编程.juc_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

import static com.wujt.并发编程.util.ConcurrentUtils.sleep;

/**
 * StampedLock : 戳锁，实现了读写锁和乐观锁（Semaphore）
 * <p>
 * API:
 * public long writeLock()
 * public long readLock()
 * public long tryWriteLock()
 * public long tryReadLock()
 * <p>
 * 获取乐观读锁，获取即有效；不会阻塞其他写锁的获取，同时写锁获取后，乐观读锁失效。
 * 该锁的一个特点是适用于读多写少的场景，因为获取读锁只是使用位操作进行检验，不涉及 CAS 操作，所以效率会高很多，
 * 但是同时由于没有使用真正的锁，在保证数据一致性上需要拷贝一份要操作的变量到方法栈，并且在操作数据时候可能其它写线程已经修改了数据，
 * public long tryOptimisticRead()
 * public boolean validate(long stamp)
 * <p>
 * 尝试将读锁转化为写锁：当前仅有自己获取读锁时。
 * 1.当前锁已经是写锁模式了。
 * 2.当前锁处于读锁模式，并且没有其他线程是读锁模式
 * 3.当前处于乐观读模式，并且当前写锁可用。
 * public long tryConvertToWriteLock(long stamp)
 * <p>
 * 特点：
 * 不具备可重入性，使用时需要注意死锁。
 * 提供了自旋的特性；当获取锁失败后；入队列前都会进行一定的自旋。
 * 如何避免发生死锁：
 * StampedLock 的读写锁都是不可重入锁，所以当获取锁后释放锁前，不应该再调用会获取锁的操作，以避免产生死锁。
 *
 * @author wujt
 */
public class StampedLockDemo {

    private static Boolean flag = true;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Map<String, String> map = new HashMap<>();
        StampedLock lock = new StampedLock();
        /**
         * 测试写锁会阻塞读锁，读锁能并发进行读操作。
         */
        testAPI(executor, map, lock);


        ConcurrentUtils.sleep(2000);
        System.out.println();
        System.out.println("test optimistic locking");
        /**
         *  测试乐观读锁： 乐观读锁可以并发获取，但是遇到写锁获取失效；需要通过validate 方法进行校验。
         */
        testOptimisticLock(executor, lock);


        ConcurrentUtils.sleep(2000);
        System.out.println();
        System.out.println("test optimistic locking2:");
        /**
         * 测试乐观读锁升级为悲观读锁。
         * 当乐观读锁获取成功后，当使用读取的数据时；可能会造成
         */
        testOptimisticLock2(executor, lock);


        ConcurrentUtils.sleep(5000);
        System.out.println();
        System.out.println("test lock convert");

        /**
         *  测试锁转换，读锁转换为写锁
         */
        testConvert(executor, lock);

        ConcurrentUtils.sleep(1000);
        System.out.println();
        System.out.println(" test dead lock");
        // 测试死锁和不可重入性
        testDeadlockAndNonReentrancy(executor, lock);

        ConcurrentUtils.stop(executor);
    }

    private static void testDeadlockAndNonReentrancy(ExecutorService executor, StampedLock lock) {
        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                System.out.println("获取读锁，开始操作");
                long read = lock.readLock();
                try {
                    System.out.println(" 写锁中获取读锁，没有产生死锁");
                } finally {
                    lock.unlockRead(read);
                }

            } finally {
                lock.unlockWrite(stamp);
            }
        });
    }

    private static void testOptimisticLock2(ExecutorService executor, StampedLock lock) {
        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                if (lock.validate(stamp)) {
                    System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                    // 对共享变量进行拷贝使用；

                }
            } finally {
                lock.unlock(stamp);
            }
        });
        sleep(1);
        executor.submit(() -> {
            // 乐观读锁不会阻塞写锁获取，写锁会导致乐观读锁失效。
            long stamp = lock.writeLock();
            try {
                System.out.println(Thread.currentThread().getName() + " -> " + "Write Lock acquired");
                sleep(1000);
            } finally {
                lock.unlock(stamp);
                System.out.println(Thread.currentThread().getName() + " -> " + "Write done");
            }
        });
        executor.submit(() -> {
            // ConcurrentUtils.sleep(1);
            long stamp = lock.tryOptimisticRead();
            try {
                if (lock.validate(stamp)) {
                    System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                } else {
                    System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                    stamp = lock.readLock();
                    System.out.println(Thread.currentThread().getName() + " -> " + "Read Lock get success ");
                }

            } finally {
                lock.unlock(stamp);
            }
        });
    }

    private static void testConvert(ExecutorService executor, StampedLock lock) {
        executor.submit(() -> {
            // 获取读锁
            long stamp = lock.readLock();
            try {
                System.out.println("通过读锁执行了部分业务");
                if (flag) {
                    System.out.println("如果条件满足，我将获取写锁。");
                    // 认为当前只有自己获取到了读锁；并没有其他的线程占用读锁
                    stamp = lock.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock，I will wait");
                        stamp = lock.writeLock();
                    }
                    flag = false;
                }
                System.out.println("我获取到了写锁，我要执行任务了");
            } finally {
                lock.unlock(stamp);
            }
        });
    }

    private static void testOptimisticLock(ExecutorService executor, StampedLock lock) {
        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                sleep(1);
                System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                // 乐观锁失效
                sleep(2);
                System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
            } finally {
                lock.unlock(stamp);
            }
        });
        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                // 其他线程获取了乐观读锁，仍然可以并发获取乐观读锁。
                sleep(1);
                System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
                sleep(2);
                System.out.println(Thread.currentThread().getName() + " -> " + "Optimistic Lock Valid: " + lock.validate(stamp));
            } finally {
                lock.unlock(stamp);
            }
        });

        executor.submit(() -> {
            // 乐观读锁不会阻塞写锁获取，写锁会导致乐观读锁失效。
            long stamp = lock.writeLock();
            try {
                System.out.println(Thread.currentThread().getName() + " -> " + "Write Lock acquired");
                sleep(2);
            } finally {
                lock.unlock(stamp);
                System.out.println(Thread.currentThread().getName() + " -> " + "Write done");
            }
        });
    }

    private static void testAPI(ExecutorService executor, Map<String, String> map, StampedLock lock) {
        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                sleep(1000);
                map.put("foo", "bar");
            } finally {
                lock.unlockWrite(stamp);
            }
        });

        Runnable readTask = () -> {
            long stamp = lock.readLock();
            try {
                System.out.println(map.get("foo"));
                sleep(1000);
            } finally {
                lock.unlockRead(stamp);
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
    }


}