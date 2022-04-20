package com.wujt.network.nio;

import java.nio.ByteBuffer;

/**
 * Buffer:
 * <p>
 * fields:
 * 1、capacity——容量。表示Buffer最多可以保存元素的数量，在创建Buffer对象是设置，设置后不可改变。
 * 2、limit——可以使用的最大数量。limit在创建对象时默认和capacity相等，若是有专门设置，其值不可超过capacity，表示可操作元素个数。capacity=100，表示最多可以保存100个，假设现在只存了20个就要读取，这是limit会被设为20.
 * 3、position——当前所操作元素的位置索引，从0开始，随着get方法和put方法进行更新。
 * 4、mark——用来暂存position。起中间变量的作用，使用mark保存当前position的值后，进行相关操作position会产生变化，若使用reset()方法，便会根据mark的值去恢复position操作前的索引。mark默认-1，取值不可超过position。
 * 大小关系：mark<=position<=limit<=capacity
 * 初始化方法：clear()，用于写数据前初始化相关属性值。
 *
 *
 * @author wujt
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(20);

    }
}
