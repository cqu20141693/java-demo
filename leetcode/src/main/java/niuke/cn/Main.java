package niuke.cn;

import java.util.Scanner;

/**
 * @author gow
 * @date 2022/1/15
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        int maxSubSeq = main.maxSubSeq(new int[]{1,2,3,4,2}, 6);
        System.out.println(maxSubSeq);
    }
    public int maxSubSeq(int[] arr, int sum) {
        // 数组元素为正整数，子序列和一定递增
        // 遍历数组，判断连续子序列和是否为sum,等于更新result

        int result = -1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < sum) {
                int currSum = arr[i];
                for (int j = i + 1; j < arr.length; j++) {
                    currSum += arr[j];
                    // 如果子序列和为sum
                    if (currSum == sum) {
                        int subLen = j - i + 1;
                        if (subLen > result) {
                            result = subLen;
                        }
                        break;
                    } else if (currSum > sum) { // 如果大于则跳出循环
                        break;
                    }
                }
            } else if (arr[i] == sum) {
                if (result < 1) {
                    result = 1;
                }
            }
        }
        // 特殊值处理
        if (arr[arr.length - 1] == sum) {
            if (result < 1) {
                result = 1;
            }
        }
        return result;
    }
}
