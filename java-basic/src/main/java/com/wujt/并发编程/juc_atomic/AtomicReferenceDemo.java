package com.wujt.并发编程.juc_atomic;


import com.wujt.函数式编程.User;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;

/**
 * @author wujt
 */
public class AtomicReferenceDemo {
   static AtomicReference<User> userAtomicReference = new AtomicReference<User>();
    public static void main(String[] args) {
        User user = new User();
        user.setName("taoge");
        userAtomicReference.set(user);
        User user1 = userAtomicReference.accumulateAndGet(user, new BinaryOperator<User>() {
            @Override
            public User apply(User user, User user2) {
                return null;
            }
        });
    }
}
