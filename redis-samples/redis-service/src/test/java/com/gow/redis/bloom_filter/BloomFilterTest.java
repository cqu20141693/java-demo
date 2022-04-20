package com.gow.redis.bloom_filter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/3
 */
public class BloomFilterTest {

    @Test
    @DisplayName("test BloomFilter")
    public void testBloomFilter(){
        // 初始化一个存储string数据的布隆过滤器，初始化大小1w, 错误率为0.1%
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 10000, 0.001);

        // 添加数据
        bloomFilter.put("hello world!");

        // 判断是否存在，false则表示一定不存在； true表示可能存在
        boolean ans = bloomFilter.mightContain("hello world!");
        System.out.println(ans);

        ans = bloomFilter.mightContain("hello world");
        System.out.println(ans);
    }
}
