package com.wujt.array;

import java.util.Arrays;

/**
 * @author wujt
 */
public class MatrixFindK {

    public int kthSmallest1(int[][] matrix, int k) {
        int raw=matrix.length,clumn=raw;
        int[] sorts=new int[raw*clumn];
        int index = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                sorts[index++] = num;
            }
        }
        Arrays.sort(sorts);
        return sorts[k - 1];
    }

    /**
     * 矩阵具有的性质从左到右递增，从上到下递增：最左上角的数据最小，最右下角的数据最大
     * 一个很好的思路是利用二分数据定位，计算比之小的数据量和k的大小，如果
     *
     * @param matrix
     * @param k
     * @return
     */
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int left = matrix[0][0];
        int right = matrix[n - 1][n - 1];
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            // 不少于时，表示需要右移到中间位置
            if (check(matrix, mid, k, n)) {
                right = mid;
            } else {
                // 少于时表示需要左移到中间位置之前
                left = mid + 1;
            }
        }
        return left;
    }

    public boolean check(int[][] matrix, int mid, int k, int n) {
        int i = n - 1;
        int j = 0;
        int num = 0;
        while (i >= 0 && j < n) {
            // 当前列的比较值
            if (matrix[i][j] <= mid) {
                //当前位置以上的数据都比较mid小
                num += i + 1;
                //左移一列
                j++;
            } else {
                // 上移一行比较，当前行的后续数据都比mid大
                i--;
            }
        }
        // 最后一定出现num小于k
        return num >= k;
    }

}
