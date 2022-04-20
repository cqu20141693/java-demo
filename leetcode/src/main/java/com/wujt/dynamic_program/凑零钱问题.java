package com.wujt.dynamic_program;

import java.util.Scanner;

/**
 * ：给你 k 种面值的硬币，面值分别为 c1, c2 ... ck，每种硬币的数量无限，再给一个总金额 amount，
 * 问你最少需要几枚硬币凑出这个金额，如果不可能凑出，算法返回 -1 。
 * <p>
 * 假设需要最少的硬币成立；这减少一个硬币也是能处理的；所以知道最后一个硬币；
 * 所以有dp(0)=0;dp(<0)=-1;dp(n)=min(dp(n-coin)+1|coin) n>0
 *
 * @author wujt
 */
public class 凑零钱问题 {

    /**
     * 动态规划子问题分解问题；
     *
     *
     */
    private static class RaiseChange {

        /**
         * @param cions
         * @param amount
         * @return
         */
        public int count(int[] cions, int amount) {
            int[] amounts = new int[amount + 1];

            // 保证当前每个子问题的无解
            for (int i = 1; i <= amount; i++) {
                amounts[i] = amount + 1;
            }
            // 初始化解
            amounts[0] = 0;
            for (int i = 1; i < amount; i++) {

                // 存在多种决策。
                for (int cion : cions) {
                    // 如果问题无解；直接滤过
                    if (i - cion < 0) continue;
                    // 表示子问题存在解
                    // 只有当i-coin=0 时是存在的初始解；
                    if (amounts[i] > amounts[i - cion] + 1) { // 找出当前问题的最优解
                        amounts[i] = amounts[i - cion] + 1;
                    }
                }
            }
            return amounts[amount] == amount + 1 ? -1 : amounts[amount];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            //int n=Integer.parseInt(sc.nextLine());
            String[] msg = sc.nextLine().split(" ");
            int[] coins = new int[msg.length];
            for (int i = 0; i < msg.length; i++) {
                String s = msg[i];
                coins[i] = Integer.parseInt(s);
            }
            int amount = Integer.parseInt(sc.nextLine());
            System.out.println(coinChange(coins, amount));
        }
    }

    private static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 1; i < dp.length; i++)
            dp[i] = amount + 1;

        // base
        dp[0] = 0;

        // 求解dp[i]
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin < 0) continue;
                // 如果子问题找不到解初始值就会是0；
                // 所以只需要计算出n-coins 的所有子问题的解后就可以获取问题的解
                dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
            }

        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }
}
