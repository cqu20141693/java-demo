package com.wujt.dynamic_program;

/**
 * @author wujt
 */
public class Fib {
    public int fib(int n) {
        if (n < 1) return 0;
        if (n == 1 || n == 2) return 1;
        // 菲波拉契数已经有了状态方程，且明确了F(n)=F(n-1)+F(n-2)
        // 并且base case f（0）=0;f(1)=1;f(2)=1
        int[] dps = new int[n + 1];
        // 初始化base case；
        dps[1] = 1;
        dps[2] = 1;
        // 自底向上进行状态转移
        for (int i = 3; i <= n; i++) {
            // 状态转移方程
            dps[i] = dps[i - 1] + dps[i - 2];
        }
        return dps[n];

    }
}
