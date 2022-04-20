package com.wujt.collections.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wujt
 */
public class LinkedBlockQueueDemo {
    public static void main(String[] args) {

        LinkedBlockingQueue<Integer> idleQueue = new LinkedBlockingQueue<>(512);
        LinkedBlockingQueue<Integer> workQuue = new LinkedBlockingQueue<>();
        try {
            idleQueue.put(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 1. 自定一个一个数据库的连接装饰对象（需要连接池和real连接和proxyConnection），当获取连接的时候返回的时候代理连接，当对连接进行关闭的时候不是直接关闭连接，是将连接归还到连接池中

        // 2. 利用jdk代理对连接进行方法代理，对close方法进行特殊处理，将其收回到连接池空闲队列中，其他的方法调用直接调用real连接进行调用


        // 定义一个连接池，需要支持工作队里和空闲队列两个队列，并且支持获取连接和关闭连接
        // 首先是需要初始化连接池，初始化创建连接，并创建将代理连接放到空闲连接池中
        // 实现获取连接的接口，首先是从空闲池中获取连接，如果获取完成，则直接使用，如果获取不到，则使用超时获取的方式，如果超时期间获取成功，则可以直接使用，并将当前连接放入工作队列汇中，
        // 如果获取失败，则获取锁去检查工作队列中的第一个元素，判断其是否已经超时，如果已经超时，则抛出获取连接超时的异常，由业务层处理，比如暂停消费kafka数据
        //  当一个数据库操作完成后，需要close连接，此时则调用连接池的close方法进行关闭，将工作队列中的连接移除，同时添加到空闲队列中
        // 还需要提供一个关闭连接池的接口，将所有的连接都关闭掉；依次取出空闲队列连接进行关闭，并给工作连接添加监听器，


        // 多个库，每个库一个数据源（连接池），当获取连接的时候，首先是获取连接池，当第一次时需要进行初始化（这里需要加锁）二次判断，然后获取连接。


        ArrayBlockingQueue<Integer> arrayBlockingQueue=new ArrayBlockingQueue(512);
        try {
            arrayBlockingQueue.put(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
