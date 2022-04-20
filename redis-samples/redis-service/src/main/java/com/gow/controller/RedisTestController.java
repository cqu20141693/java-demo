package com.gow.controller;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.gow.redis.operation.RedisAdvanceFacade;
import com.gow.redis.operation.RedisSetFacade;
import com.gow.redis.support.BloomFilterHelper;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/8/27
 */
@RestController
@RequestMapping("redis/test")
public class RedisTestController {

    @Autowired
    private RedisAdvanceFacade redisAdvanceFacade;

    @Autowired
    private RedisSetFacade redisSetFacade;

    @GetMapping("keysByScan")
    public List<String> keysByScan(@RequestParam("prefix") String prefix, @RequestParam("scanCount") Long scanCount) {
        return redisAdvanceFacade.keysByScan(prefix, scanCount);
    }

    @GetMapping("setDiffAndStore")
    public Long setDiffAndStore(@RequestParam("keys") Set<String> keys,
                                @RequestParam("destKey") String destKey) {
        return redisSetFacade.sDiffStore(keys, destKey);
    }

    @GetMapping("bloomAdd")
    public Boolean bloomAdd(@RequestParam("key") String key,
                            @RequestParam("destKey") String destKey) {
        Funnel<CharSequence> funnel = Funnels.stringFunnel(Charsets.UTF_8);
        BloomFilterHelper<CharSequence> helper = new BloomFilterHelper<>(funnel, 10000, 0.001);
        redisAdvanceFacade.bloomSet(helper, key, destKey);
        return true;
    }
    @GetMapping("bloomFilter")
    public Boolean bloomFilter(@RequestParam("key") String key,
                            @RequestParam("destKey") String destKey) {
        Funnel<CharSequence> funnel = Funnels.stringFunnel(Charsets.UTF_8);
        BloomFilterHelper<CharSequence> helper = new BloomFilterHelper<>(funnel, 10000, 0.001);
        return redisAdvanceFacade.bloomFilter(helper, key, destKey);
    }
}
