package com.gow.aop.proxyfactory.join_point;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class Service implements IService {
    @Override
    public void say(String name) {
        System.out.println("hello:" + name);
    }
}
