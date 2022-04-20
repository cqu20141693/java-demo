package com.wujt.proxy;

/**
 * @author wujt
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void echo(String value) {
        System.out.println("hello " + value);
    }
}
