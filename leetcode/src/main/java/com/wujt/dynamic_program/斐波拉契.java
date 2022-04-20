package com.wujt.dynamic_program;

/**
 * 斐波拉契并不是一个真正意义上的动态规划：
 * 他其实是已经你知道了问题分解后的关系和base解
 * f(1)=1;f(0)=0;
 * f(n)=f(n-1)+f(n-2)
 * 因此可以直接递归和求解
 *
 * @author wujt
 */
public class 斐波拉契 {

    /**
     *  一个人可以一次走一步和两步台阶；问走上n阶台阶有多少中走法；
     *
     *
     *
     */
    private static class Problem {
        /**
         * 直接存在状态转移方程： f(n)=f<n-1)+f(n-2)
         *
         * @param n
         * @return
         */
        public int fibonacci(int n) {

            int[] dp = new int[n + 1];
            dp[0] = 0;
            dp[1] = 1;
            dp[2] = 1;
            for (int i = 3; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }

        /**
         * 子问题分解解决
         * @param n
         * @return
         */
        public int fibonacci2(int n) {

            int[] dp = new int[n + 1];
            // 子问题初始解
            dp[0] = 0;
            dp[1] = 1;
            dp[2] = 1;
            for (int i = 3; i <= n; i++) {
                // 直接利用状态方程；
                // 其他地方的状态方程可能会比较复杂，比如取最小值。
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }

    public static void main(String[] args) {

        int fib1 = fib1(5);
        int fib2 = fib2(10);
        Problem problem = new Problem();
        int fibonacci = problem.fibonacci(5);
        int fibonacci1 = problem.fibonacci(10);
    }


    /**
     * 规划： 记录下重复使用的子问题解；让后自底向上的求解问题；
     *
     * @param n
     * @return
     */
    private static int fib2(int n) {
        if (n == 2 || n == 1)
            return 1;
        int prev = 1, curr = 1;
        for (int i = 3; i <= n; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }

    /**
     * 递归
     *
     * @param n
     * @return
     */
    private static int fib1(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib1(n - 1) + fib1(n - 2);
    }
}
