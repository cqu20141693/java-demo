package com.gow.redis.utils;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

@Component
public class RedisScriptUtils implements InitializingBean {


    private DefaultRedisScript<String> registerScript = new DefaultRedisScript<>();
    private DefaultRedisScript<Boolean> disconnectScript = new DefaultRedisScript<>();
    private DefaultRedisScript<Long> refreshScript = new DefaultRedisScript<>();

    public DefaultRedisScript<Void> getBloomSetScript() {
        return bloomSetScript;
    }

    public DefaultRedisScript<Boolean> getBloomFilterScript() {
        return bloomFilterScript;
    }

    private DefaultRedisScript<Void> bloomSetScript = new DefaultRedisScript<>();
    private DefaultRedisScript<Boolean> bloomFilterScript = new DefaultRedisScript<>();

    public DefaultRedisScript<String> getRegisterScript() {
        return registerScript;
    }

    public DefaultRedisScript<Boolean> getDisconnectScript() {
        return disconnectScript;
    }

    public DefaultRedisScript<Long> getRefreshScript() {
        return refreshScript;
    }

    public DefaultRedisScript<List> getRateLimiterScript() {
        return rateLimiterScript;
    }

    private DefaultRedisScript<List> rateLimiterScript = new DefaultRedisScript<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        registerScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "lua/register.lua")));
        registerScript.setResultType(String.class);
        disconnectScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "lua/disconnect.lua")));
        disconnectScript.setResultType(Boolean.class);
        refreshScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "lua/refresh.lua")));
        refreshScript.setResultType(Long.class);
        bloomSetScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "lua/bloomSet.lua")));
        bloomSetScript.setResultType(Void.class);

        bloomFilterScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "lua/bloomFilter.lua")));
        bloomFilterScript.setResultType(Boolean.class);

        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("lua/request_rate_limiter.lua"));
        rateLimiterScript.setScriptSource(scriptSource);
        rateLimiterScript.setResultType(List.class);

    }


}
