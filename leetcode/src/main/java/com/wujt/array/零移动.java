package com.wujt.array;

/**
 * @author gow
 * @date 2021/9/9
 */
public class 零移动 {

    public static void main(String[] args) {
        零移动 zeroMove = new 零移动();
        zeroMove.moveZeroes(new int[]{0,1,0,3,12});
    }
    public void moveZeroes(int[] nums) {
        // 利用空间换时间
        if (nums == null || nums.length < 2) {
            return;
        }
        int j = 0;
        for (int i = 0; i < nums.length; i++) {

            if (nums[i] != 0) {
                nums[j++] = nums[i];
            }
        }
        for (int j2 = j; j2 < nums.length; j2++) {
            nums[j2] = 0;
        }
    }
}
