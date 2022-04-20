package com.gow.cache.custom;

import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/7/30
 */
public class CustomCacheTest {

    public static void main(String[] args) throws InterruptedException {
        CacheCleaner cacheCleaner = new CacheCleaner();
        cacheCleaner.init();

        CustomCache customCache = new CustomCache(cacheCleaner);
        customCache.init();

        addCache(customCache);
        int i = 0;
        while (i++ < 3) {

           // addCache(customCache);
            Thread.sleep(2 * 1000);
        }
        // cacheCleaner.destroy();
    }

    private static void addCache(CustomCache customCache) {

        customCache.add("groupKey", "sn", "cmdTag", 3000);

        customCache.add("groupKey", "sn", "toMillis-3", TimeUnit.SECONDS.toMillis(3));
        customCache.add("groupKey", "sn", "toMillis-10", TimeUnit.SECONDS.toMillis(100));


        customCache.add("groupKey", "sn", "toMicros", TimeUnit.SECONDS.toMicros(3));

        customCache.add("groupKey", "sn", "toNanos", TimeUnit.SECONDS.toNanos(3));

    }
}
