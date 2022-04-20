package com.wujt.dynamic_program;

/**
 * 动态规划决策问题：
 *
 * 每一次决策都记录一个状态点；
 *
 * @author wujt
 */
public class 背包问题 {
    /**
     * 对于一组不同重量、不可分割的物品，我们需要选择一些装入背包，在满足背包最大重量限制的前提下，背包中物品总重量的最大值是多少呢？
     * <p>
     * 其主要的思想是做选择：
     * 要么选择他；要么放弃直接进行下一个选择；
     */
    private static class Problem1 {
        private int maxW = Integer.MIN_VALUE; // 结果放到maxW中
        private int[] weight = {2, 2, 4, 6, 3}; // 物品重量
        private int n = 5; // 物品个数
        private int w = 9; // 背包承受的最大重量

        // 解决重复子问题，当处理到一个物品时，可能存在多个情况导致当前的重量一样；
        private boolean[][] mem = new boolean[5][10]; // 备忘录，默认值false

        /**
         * 决策第i个物品是否要加入物品
         * <p>
         * 前置检查：
         * 当前重量已经达到最大值时，保留最大值，直接返回
         * 当前遍历值为总物品个数；判断当前重量是否大于保留最大值；大于重新赋值保留最大值，并直接返回
         * <p>
         * 开始回溯遍历：
         * 将处理的第i个物品直接忽略，处理i+1;
         * 处理第i个物品： 如果满足当前重量+第i个物品重量小于最大重量；将其加入当前重量；然后处理i+1个物品
         * <p>
         * 回溯思想： 会将所有可能的路走完；直到满足达到最大值或者是处理到最后一个物品。
         * <p>
         * 时间复杂度为2^n
         *
         * @param i
         * @param cw
         */
        public void f(int i, int cw) { // 调用f(0, 0)
            if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
                if (cw > maxW) maxW = cw;
                return;
            }

            f(i + 1, cw); // 选择不装第i个物品
            if (cw + weight[i] <= w) {
                f(i + 1, cw + weight[i]); // 选择装第i个物品
            }
        }

        /**
         * 该方式其实已经和动态规划差不多了；在每一个层级都存储了对应的数据
         *
         * @param i
         * @param cw
         */
        public void f1(int i, int cw) { // 调用f(0, 0)
            if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
                if (cw > maxW) maxW = cw;
                return;
            }
            if (mem[i][cw]) return; // 重复状态
            mem[i][cw] = true; // 记录(i, cw)这个状态
            f1(i + 1, cw); // 选择不装第i个物品
            if (cw + weight[i] <= w) {
                f1(i + 1, cw + weight[i]); // 选择装第i个物品
            }
        }

        /**
         * @param weight 物品重量
         * @param n      物品个数
         * @param w      背包可承载重量
         *               <p>
         *               时间复杂度n*m
         * @return
         */
        public int knapsack(int[] weight, int n, int w) {
            boolean[][] states = new boolean[n][w + 1]; // 默认值false
            states[0][0] = true;  // 第一行的数据要特殊处理，可以利用哨兵优化
            if (weight[0] <= w) {
                states[0][weight[0]] = true;
            }
            for (int i = 1; i < n; ++i) { // 动态规划状态转移， 做决策循环
                for (int j = 0; j <= w; ++j) {// 不把第i个物品放入背包
                    // 将上一层中每个状态转移到当前层；
                    if (states[i - 1][j]) states[i][j] = states[i - 1][j];
                }
                for (int j = 0; j <= w - weight[i]; ++j) {//把第i个物品放入背包
                    //如果上一次决策状态为真，并且加入当前重量后仍然可以放入背包；则将当前层的当前重量状态设置为真
                    if (states[i - 1][j]) states[i][j + weight[i]] = true;
                }
            }
            // 依次每次都将上层的状态做出选择后放到下层中；这样就保证了最后一层具有所有的可能的重量的汇总（值为真的数据）；
            for (int i = w; i >= 0; --i) { // 输出结果
                if (states[n - 1][i]) return i;
            }
            return 0;
        }

        /**
         * 首先是画出决策树；
         * 然后找出决策路径计算方式；
         * 通过循环进行状态转移。
         *
         * @param weight
         * @param n
         * @param w
         * @return
         */
        public int dpbeibao(int[] weight, int n, int w) {
            // n 个物件，一共有n次决策： 决策出结果最大为w
            boolean[][] states = new boolean[n][w+1];

            // 初始化第一次决策,
            states[0][0] = true; // 不选择第一个物品；肯定为真
            if (weight[0] < w) { // 如果第一个物品重量不大于w; 则可以
                states[0][weight[0]] = true;
            }

            for (int i = 1; i < n; i++) { // 准备进行状态转移，依次进行每一个物品决策
                // 不选择第i个物品,直接将上一层的结果放到当前层
                for (int j = 0; j < w; j++)
                    states[i][j] = states[i - 1][j];
                // 选择第i个物品 ； 保证上一层可达的重量应该小于w-weight[i]
                // 这里保证了重量不会超过w
                for (int j = 0; j <= w - weight[i]; j++) {
                    // 找到小于可达重量中为真的,新增当前层可达（j+weight[i]）
                    if (states[i - 1][j]) states[i][j + weight[i]] = true;
                }
            }

            // 最后一层中从右到左中为第一个为真的即为最大值
            for (int i = w; i > 0; i--) {
                if (states[n - 1][w - 1]) return w;
            }
            return 0;
        }

        public int dpbeibao2(int[] weight, int n, int w) {
            boolean[] states = new boolean[w+1];
            // 初始化初始状态
            states[0] = true;

            // 循环决策将物品放入背包，进行重量状态更新
            for (int i = 0; i < n; i++) {
                // 保证可达重量都小于w
                for (int j = 0; j <= w - weight[i]; j++) {
                    // 需要寻找当前可以当前层之前可达重量小于w-weight[i]
                    // 更新当前可达的重量
                    if (states[j]) states[j + weight[i]] = true;
                }
            }
            for (int i = w; i > 0; i--) {
                if (states[w - 1]) return w;
            }
            return 0;
        }


    }

    /**
     * 首先画决策树：
     * <p>
     * 对于一组不同重量、不同价值、不可分割的物品，我们选择将某些物品装入背包，在满足背包最大重量限制的前提下，背包中可装入物品的总价值最大是多少呢？
     */

    private static class Problem2 {


        private int maxV = Integer.MIN_VALUE; // 结果放到maxV中
        private int[] items = {2, 2, 4, 6, 3};  // 物品的重量
        private int[] value = {3, 4, 8, 9, 6}; // 物品的价值
        private int n = 5; // 物品个数
        private int w = 9; // 背包承受的最大重量

        /**
         * 利用回溯直接枚举所有的可能；
         * @param i
         * @param cw
         * @param cv
         */
        public void f(int i, int cw, int cv) { // 调用f(0, 0, 0)
            if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
                if (cv > maxV) maxV = cv;
                return;
            }
            f(i + 1, cw, cv); // 选择不装第i个物品
            if (cw + items[i] <= w) {
                f(i + 1, cw + items[i], cv + value[i]); // 选择装第i个物品
            }
        }

        /**
         * 当处理到一个可达重量时；维护起对应的最大值
         *
         * @param weight
         * @param value
         * @param n
         * @param w
         * @return
         */
        public int dpbeibao(int[] weight, int[] value, int n, int w) {
            // 其中状态的值如果大于0；表示可达，并且对应的值为当前可达的最大价值
            int[] states = new int[w+1];
            final int initValue = -1;
            for (int i = 0; i < w; i++) {
                states[i] = initValue;
            }
            states[0] = 0;
            for (int i = 0; i < n; i++) {

                // 下一种决策只有一种；
                for (int j = 0; j <= w - weight[i]; j++) {
                    // 表示当前是可达，并且已经存在最大重量
                    if (states[j] > initValue) {
                        // 如果可达重量当前重量最大值小于此次可达；则更新值
                        if (states[j + weight[i]] < states[j] + value[i])
                            states[j + weight[i]] = states[j] + value[i];
                    }
                }

            }

            for (int i = w; i > 0; i--) {
                if (states[w ] > initValue) {
                    System.out.println("max=" + w);
                    return states[w];
                }
            }
            return 0;
        }

        public static int knapsack3(int[] weight, int[] value, int n, int w) {
            int[][] states = new int[n][w+1];
            for (int i = 0; i < n; ++i) { // 初始化states
                for (int j = 0; j < w+1; ++j) {
                    states[i][j] = -1;
                }
            }
            states[0][0] = 0;
            if (weight[0] <= w) {
                states[0][weight[0]] = value[0];
            }
            for (int i = 1; i < n; ++i) { //动态规划，状态转移
                for (int j = 0; j <= w; ++j) { // 不选择第i个物品
                    if (states[i-1][j] >= 0) states[i][j] = states[i-1][j];
                }
                for (int j = 0; j <= w-weight[i]; ++j) { // 选择第i个物品
                    if (states[i-1][j] >= 0) {
                        int v = states[i-1][j] + value[i];
                        if (v > states[i][j+weight[i]]) {
                            states[i][j+weight[i]] = v;
                        }
                    }
                }
            }
            // 找出最大值
            int maxvalue = -1;
            for (int j = 0; j <= w; ++j) {
                if (states[n-1][j] > maxvalue) maxvalue = states[n-1][j];
            }
            return maxvalue;
        }
    }





    public static void main(String[] args) {
        testProblem1();
        int[] items = {2, 2, 4, 6, 3};  // 物品的重量
        int[] value = {3, 4, 8, 9, 6}; // 物品的价值
        int n = 5; // 物品个数
        int w = 9; // 背包承受的最大重量
        Problem2 problem2 = new Problem2();
        problem2.f(0, 0, 0);
        System.out.println(problem2.maxV);

        int dpbeibao = problem2.dpbeibao(problem2.items, problem2.value, problem2.n, problem2.w);

    }

    private static void testProblem1() {
        Problem1 problem1 = new Problem1();
        problem1.f(0, 0);
        System.out.println(problem1.maxW);

        int[] weight = {2, 2, 4, 6, 3}; // 物品重量
        int n = 5; // 物品个数
        int w = 9; // 背包承受的最大重量
        int knapsack = problem1.knapsack(weight, n, w);
        int dpbeibao = problem1.dpbeibao(weight, n, w);
        int dpbeibao21 = problem1.dpbeibao2(weight, n, w);

        int[] weight1 = {2, 2, 4, 6, 3, 2, 4}; // 物品重量
        int n2 = 7; // 物品个数
        int w2 = 11; // 背包承受的最大重量
        int knapsack1 = problem1.knapsack(weight1, n2, w2);
        int dpbeibao1 = problem1.dpbeibao(weight1, n2, w2);
        int dpbeibao2 = problem1.dpbeibao2(weight1, n2, w2);
    }


}
