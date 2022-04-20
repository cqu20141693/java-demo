package com.gow.aop.aspectj.service;

import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/26 0026
 */
@Component
public class CommonServiceImpl implements CommonService {

    public void greeting() {
        System.out.println("hello");
    }

    public void welcome() {
        System.out.println("welcome");
    }

    public void exception() {
        throw new RuntimeException("exception");
    }
}
