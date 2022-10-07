package com.gow;

import com.gow.algorithm.SnowFlakeIdGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * wcc 2022/9/28
 */
public class SnowflakeTest {

    @Test
    public void testIdGenerator() {

        SnowFlakeIdGenerator snowFlake = new SnowFlakeIdGenerator(2, 3);
        HashSet<Long> set = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < (1 << 20); i++) {
            set.add(snowFlake.nextId());
        }
        System.out.println("use time " + (System.currentTimeMillis() - start));
        assert set.size() == 1 << 12 : "snakeFlow has repeat";

        System.out.println("success");

    }
}
