package com.wujt.array;

/**
 * @author wujt
 */
public class ArrayTest {
    public static void main(String[] args) {
        MaxArea maxArea = new MaxArea();
        int[] ints = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int maxArea1 = maxArea.maxArea(ints);
        int[] ints1 = {3, 1, 2, 5, 2, 4};
        int maxArea2 = maxArea.maxArea(ints1);
        System.out.println(maxArea1 + ":" + maxArea2);
    }
}
