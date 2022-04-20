package com.gow.expression.test.env;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/13
 */
@Component
@Slf4j
public class EnvExpressionTest {

    @Value("${spring.application.name}")
    private String name;

    @PostConstruct
    public void init() {
        log.info("name={}", name);
    }
}
