package com.wujt.java11;

import java.util.Locale;
import java.util.function.BiFunction;

/**
 * @author gow 2021/06/13
 */
public class LambdaVarDemo {

    public static void main(String[] args) {
        BiFunction function = (var s, var k) -> {
            String result = s + "" + k;
            System.out.println(result);
            return result;
        };
        //function.apply("hello", "world");
        BiFunction function1 = function.andThen((var value) -> {
            String v = (String) value;
            String upperCase = v.toUpperCase(Locale.ROOT);
            System.out.println(upperCase);
            return upperCase;
        });
        function1.apply("hello","world");
    }
}
