package com.wujt.java10;

import java.util.Optional;

/**
 * @author gow 2021/06/13
 */
public class OptionalDemo {
    public static void main(String[] args) {

        Optional.of("java stack").orElseThrow();
        Optional.of(null).orElseThrow();
    }
}
