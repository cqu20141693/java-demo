package com.gow.app.config;

import com.gow.redis.operation.RedisMQFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wujt  2021/5/14
 */
@Component
public class KeyExpiredEventListener {

    @Value("${redis.key-expire-event.topic}")
    private String topic;


    @Autowired
    private RedisMQFacade template;

    @PostConstruct
    public void init() {
        template.addPatternTopic(topic);

    }
}
