package com.wujt.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/1/19
 * @description :
 */
public class StackDemo {

    public static void main(String[] args) {

        //式字符串的括号检测
        String expression = "3+2*(4+){},[]<<你好,world>>";
        String expression1 = "}3+2*(4+){},[]<<你好,world>>";
        check( expression);
        check(expression1);
    }

    private static void check( String expression) {
        Map<Character, Character> map = new HashMap<>(3);
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        Stack<Character> stack = new Stack();
        for (char c : expression.toCharArray()) {
            if (map.values().contains(c)) {
                stack.push(c);
            }
            if (map.keySet().contains(c)) {
                Character character = stack.pop();
                if (!map.get(c).equals(character)) {
                    System.out.println("括号不匹配");
                }
            }
        }
    }
}
