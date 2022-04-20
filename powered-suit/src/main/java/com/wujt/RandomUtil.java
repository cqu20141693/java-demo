package com.wujt;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author wujt
 */
public class RandomUtil {
    private static Random random = new Random();

    public static Integer getRandomNumer(Integer max) {

        return random.nextInt(max);
    }

    public static double randomDouble(Integer max, Integer round) {

        double aDouble = random.nextDouble() * max;
        BigDecimal b = new BigDecimal(aDouble);
        return b.setScale(round, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void main(String[] args) {
        double aDouble = RandomUtil.randomDouble(100,2);
    }
}
