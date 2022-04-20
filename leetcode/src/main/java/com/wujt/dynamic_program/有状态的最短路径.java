package com.wujt.dynamic_program;

/**
 * @author wujt
 */
public class 有状态的最短路径 {

    /**
     * 假设我们有一个 n 乘以 n 的矩阵 w[n][n]。矩阵存储的都是正整数。棋子起始位置在左上角，终止位置在右下角。
     * 我们将棋子从左上角移动到右下角。每次只能向右或者向下移动一位。从左上角到右下角，会有很多不同的路径可以走。
     * 我们把每条路径经过的数字加起来看作路径的长度。那从左上角移动到右下角的最短路径长度是多少呢？
     */
    private static class MatrixPath {

        /**
         * @param arr 每个点位的
         * @return
         */
        public int findRoute(int[][] arr) {
            if (arr == null || arr.length == 0 || arr[0].length == 0) {
                return 0;
            }
            int row = arr.length;
            int col = arr[0].length;
            int[][] states = new int[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++)
                    states[i][j] = Integer.MAX_VALUE;
            }
            // 初始化状态
            states[0][0] = arr[0][0];

            int j = 0;
            for (int num = 0; num < row + col - 1; num++) { // 斜着遍历矩阵
                for (int i = 0; i < row; i++) {
                    j = num - i;
                    if (j < 0) {
                        break;
                    }
                    if (j < col) {
                        //开始状态转移
                        if(i+1<row){
                            int right = states[i][j] + arr[i + 1][j];
                            if (right < states[i + 1][j]) {
                                states[i + 1][j] = right;
                            }
                        }
                        if(j+1<col){
                            int down = states[i][j] + arr[i][j + 1];
                            if (down < states[i][j + 1]) {
                                states[i][j + 1] = down;
                            }
                        }
                    }
                }
            }
            return states[row - 1][col - 1];
        }

        public int getPathLen(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return 0;
            }
            int row = matrix.length;
            int col = matrix[0].length;
            int[][] dp = new int[row][col];
            dp[0][0] = matrix[0][0];
            for (int i = 1; i < row; i++) {//找到第一列的dp值
                dp[i][0] = dp[i - 1][0] + matrix[i][0];
            }
            for (int j = 1; j < col; j++) {//找到第一行的dp值
                dp[0][j] = dp[0][j - 1] + matrix[0][j];
            }
            // 之后对每一个可达的点通过上，左计算最小值；即可计算出整个矩阵每个点的最小值。
            for (int i = 1; i < row; i++) {
                for (int j = 1; j < col; j++) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + matrix[i][j];//当前dp值为 从左边、上边两个位置取一个最小值，加上本身
                }
            }
            System.out.println("dp矩阵：");
            for (int[] i : dp) {
                for (int j : i) {
                    System.out.print(j + ",");
                }
                System.out.println();
            }
            return dp[row - 1][col - 1];
        }
    }




    public static void main(String[] args) {
        int[][] rectangle = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
        };
        MatrixPath matrixPath = new MatrixPath();
        matrixPath.getPathLen(rectangle);
        int min = matrixPath.findRoute(rectangle);
        System.out.println(min);
    }
}
