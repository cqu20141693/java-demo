package com.gow.redis.support;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import java.nio.charset.Charset;

/**
 * 包含了计算bitmap的核心算法，其实大部分代码都是来源于Guava库里面的BloomFilterStrategies类
 * 布隆过滤器一些的性质
 *
 * 与哈希表不同，布隆过滤器是一个大小固定的过滤器，可以通过任意大的数字来描述集合大小
 * 添加一个元素到集合中永远不会添加失败，但误报率会随着添加元素数量的增多逐渐上升，直到集合中所有位都设置位1
 * 查询一个元素是否存在会产生误报的可能
 * 不应该从集合中删除元素。例如一个元素对应k的hash函数，当我们尝试删除，可能导致将hash值相同的元素也一并删除。
 *
 * @author gow
 * @date 2021/6/24
 */
public class BloomFilterHelper<T> {

    // 散列函数
    private final int numHashFunctions;
    // 二进制大小
    private int bitSize;
    // 过滤器
    private Funnel<T> funnel;

    public BloomFilterHelper(int expectedInsertions) {
        this.funnel = (Funnel<T>) Funnels.stringFunnel(Charset.defaultCharset());
        bitSize = optimalNumOfBits(expectedInsertions, 0.03);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
