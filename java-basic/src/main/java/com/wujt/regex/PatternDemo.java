package com.wujt.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern
 * 构造器
 * public static Pattern compile(String regex)
 * <p>
 * 返回正则表达式
 * public String pattern()
 * 正则拆分
 * public String[] split(CharSequence input)
 * <p>
 * 快速匹配字符串
 * public Matcher matcher(CharSequence input)
 * <p>
 * <p>
 * <p>
 * Matcher
 * 匹配结果
 * public boolean matches()
 *
 * @author wujt
 */
public class PatternDemo {
    final static Pattern pattern = Pattern.compile("\\d+");
    final static Pattern tagPattern = Pattern.compile("\\d{8}\\w{16}");
    final static Pattern pwdPattern =
            Pattern.compile("(?!.*[\\u4E00-\\u9FA5\\s])(?!^[a-zA-Z]+$)(?!^[\\d]+$)(?!^[^a-zA-Z\\d]+$)^.{6,20}$");

    public static void main(String[] args) {


        pattern.pattern();//返回 \d+
        String[] str = pattern.split("我的QQ是:456456我的电话是:0532214我的邮箱是:aaa@aaa.com");
        Matcher matcher = pattern.matcher("22bb23");
        boolean matches = matcher.matches();
        String input = "20210910qwertyuiopasdfgh";
        boolean b = tagPattern.matcher(input).matches();


        testPwd();

    }

    private static void testPwd() {

        String pwd1 = "";
        boolean matches = pwdPattern.matcher(pwd1).matches();
        System.out.println("matcher:" + matches);
//        String pwd2 = null;
//        matches = pwdPattern.matcher(pwd2).matches();
//        System.out.println("matcher:" + matches);
        String pwd3 = "中国";
        matches = pwdPattern.matcher(pwd3).matches();
        System.out.println("matcher:" + matches);
        String pwd4 = "12345";
        matches = pwdPattern.matcher(pwd4).matches();
        System.out.println("matcher:" + matches);
        String pwd5 = "hy1234";
        matches = pwdPattern.matcher(pwd5).matches();
        System.out.println("matcher:" + matches);


    }

}
