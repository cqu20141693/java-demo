package com.gow.redis.bloom_filter;

import com.gow.redis.support.BloomFilterHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/6
 */
public class BloomFilterHelperTest {

    @Test
    @DisplayName("test BloomFilter helper")
    public void testBloomFilterHelper() {
        BloomFilterHelper<CharSequence> helper = new BloomFilterHelper<>(10);
        int[] hellos = helper.murmurHashOffset("hello");
        int[] gows = helper.murmurHashOffset("gow");
        System.out.println(hellos.length == gows.length);
    }
}
