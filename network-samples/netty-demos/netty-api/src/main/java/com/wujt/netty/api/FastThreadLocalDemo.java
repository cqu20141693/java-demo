package com.wujt.netty.api;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import java.util.concurrent.CountDownLatch;

/**
 * @author gow
 * @date 2022/2/9
 */
public class FastThreadLocalDemo {
    private static FastThreadLocal<Integer> threadLocal = new FastThreadLocal<>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        // 继承Thread,自定义InnerMap 属性用于存储线程级数据，实现数据线程级隔离
        // InnerMap利用数组存储缓存数据，FastThreadLocal 对象利用index属性存储其索引位置
        // 每次过去的时候通过索引获取数据
        CountDownLatch latch = new CountDownLatch(1);
        Runnable r = () -> {
            Integer integer = threadLocal.get();
            System.out.println("test" + integer);
            threadLocal.remove();
            latch.countDown();
        };
        FastThreadLocalThread thread = new FastThreadLocalThread(r);
        thread.start();
        latch.await();
    }
}
