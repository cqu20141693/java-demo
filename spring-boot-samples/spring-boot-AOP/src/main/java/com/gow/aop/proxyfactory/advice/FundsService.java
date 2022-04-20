package com.gow.aop.proxyfactory.advice;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class FundsService {
    //账户余额
    private Double balance = 1000d;

    //模拟充值
    public Double recharge(String userName, double price) {
        System.out.println(String.format("%s recharge %s", userName, price));
        balance += price;
        return balance;
    }

    //模拟提现
    public Double cashOut(String userName, double price) {
        if (balance < price) {
            throw new RuntimeException("余额不足!");
        }
        System.out.println(String.format("%s提现%s", userName, price));
        balance -= price;
        return balance;
    }

    //获取余额
    public Double getBalance(String userName) {
        return balance;
    }
}
