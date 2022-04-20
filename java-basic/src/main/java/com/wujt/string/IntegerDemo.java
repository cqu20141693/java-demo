package com.wujt.string;

/**
 * API :
 *
 *
 * @author gow
 * @date 2022/1/13
 */
public class IntegerDemo {
    public static void main(String[] args) {

        testParse();
    }

    private static void testParse() {
        String s="01";
        int i = Integer.parseInt(s);
        if(1<255){
            System.out.println("success");
        }
    }
}
