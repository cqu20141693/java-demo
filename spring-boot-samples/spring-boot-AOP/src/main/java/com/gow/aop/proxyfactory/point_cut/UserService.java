package com.gow.aop.proxyfactory.point_cut;

/**
 * @author gow
 * @date 2021/7/22 0022
 */
public class UserService {
    public void work(String userName){
        System.out.print(userName+",开始工作了!");
    }
}
