package com.wujt;

import java.util.Stack;

/**
 * 栈的方法是pop()获取数据，push(T) 写入数据
 *
 * @author wujt
 */
public class 有效的括号 {


    public boolean isValid(String s) {
        //String open="({[";
        //String close="]})";
        Stack<Character> stack = new Stack<>();

        // 首先是判断字符串的长度是否为2的整数倍 &1
        // 然后遍历字符串，发现左括号，入栈，发现右扩容，出栈一个，进行比对，是否匹配
        if ((s.length() & 1) == 0) {
            return false;
        }


        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) return false;
        }

        return stack.isEmpty();
    }
}
