package com.wujt; // 这里表示定义类所属的包路径，  不理解去搜索什么是java的包

/**
 * @author wujt
 */
public class HelloWorld {  //这里表示定义了java 类，public 表示共有的，class 类定义的关键字
    public static void main(String[] args) { // 这里表示这是一个可运行类的入口
        System.out.println("Hello World"); // 这里表示使用了工具包函数println 打印字符串Hello World
        String permutation = getPermutation(3, 3);
    }

    public static String getPermutation(int n, int k) {
        // 初始化排序数据
        int[] array = new int[n];
        array[0] = 1;
        for (int i = 1; i < n; i++) {
            array[i] = array[i - 1] * i;
        }

        // 计算第K个数据
        // 从左往右确定数字，判断k的值与各个排列值的大下 i*n!<k<(i+1)*n!
        // 则k的第n位数字的值为i+1;
        // 后续对m=k%(n!),j*(n-1)!<k<(j+1)*(n-1)! 直到n=1

        StringBuilder builder = new StringBuilder();
        int[] valid = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            valid[i] = 1;
        }
        k--; // 此处很具有迷惑性
        for (int i = 1; i <= n; i++) {
            int index = k / array[n - i] + 1; // 计算k所在n排列的区间
            for (int j = 1; j <= n; j++) {
                index -= valid[j];
                if (index == 0) {
                    builder.append(j);
                    valid[j] = 0;
                    break;
                }
            }
            k = k % array[n - i];
        }
        return builder.toString();
    }
}
