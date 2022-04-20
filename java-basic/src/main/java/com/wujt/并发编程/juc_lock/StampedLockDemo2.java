package com.wujt.并发编程.juc_lock;

/**
 * @author wujt
 */
public class StampedLockDemo2 {
    public static void main(String[] args) {
        /**
         * 当存在多个线程进行坐标移动和计算的时候；需要利用到戳锁，
         */
        Point point = new Point();
        double distance = point.distanceFromOrigin();
        point.moveIfAtOrigin(1.0,1.0);
       distance= point.distanceFromOrigin();
       point.move(2.0,2.0);
    }
}
