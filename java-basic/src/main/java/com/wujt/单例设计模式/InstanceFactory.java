package com.wujt.单例设计模式;

/**
 *
 * @author wujt
 */
public class InstanceFactory {

    /**
     * 利用类的初始化来保证线程安全： 对象初始化时，会获取一个锁，让类的初始化线程安全。
     */
    private static class InstanceHolder {
        private static InstanceFactory instance = new InstanceFactory();
    }

    /**
     * @return
     */
    public static synchronized InstanceFactory getCheckInstance() {
        return InstanceHolder.instance;
    }

}
