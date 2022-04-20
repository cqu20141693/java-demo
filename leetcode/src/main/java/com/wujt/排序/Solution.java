package com.wujt.排序;

import java.util.Arrays;

/**
 * @author wujt
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] ints = {1, 5, 3, 4, 6, 1, 3, 8};
        System.out.println(Arrays.toString(solution.sortArray(ints)));
    }

    public int[] sortArray(int[] nums) {
        if (nums == null || nums.length < 2) {
            return nums;
        }
        // 冒泡排序
//        return bobbleSort(nums);
//        return selectSort(nums);
//        return insertSort(nums);
            // 不稳定，logN
//        return shellSort(nums);
        // 稳定 logN
//        return mergeSort(nums);
        // 不稳定，logN
        return quickSort(nums,0,nums.length - 1);

    }

    private int[] quickSort(int[] nums, int left, int right) {
        // 快速排序，取数组最后一个数，然后将前面的数据与之比较，将小于部分放到一边，
        //并记录小于部分的索引index,遍历完后，将index+1的数据与最后一个数据swap
        // 然后再将数组分为0-index,index+2到length-1两个数组排序

        if (left >= right) { // 此处一定是大于等于
            return nums;
        }
        int partitionIndex = partition(nums, left, right);
        quickSort(nums, left, partitionIndex - 1);
        quickSort(nums, partitionIndex + 1, right);
        return nums;
    }

    private int partition(int[] nums, int left, int right) {
        int tmp = nums[right], index = left;

        for (int i = left; i < right; i++) {
            if (nums[i] < tmp) {
                swap(nums, index, i);
                index++;
            }
        }

        swap(nums, index, right);
        return index;
    }


    private int[] mergeSort(int[] nums) {
        // 归并排序，将一个数组进行二分，直到二分后的数组只有一个元素，此时直接返回数据，此时返回的数据已经排好序
        // 对二分的两个数组（以排好序）进行排序

        return merge(nums, 0, nums.length - 1);
    }

    private int[] merge(int[] nums, int left, int right) {
        if (left == right) {
            return new int[]{nums[left]};
        }
        // 计算中间index
        int mid = left + (right - left) / 2;
        // 二分排序
        int[] mergeA = merge(nums, left, mid);
        int[] mergeB = merge(nums, mid + 1, right);

        int[] result = new int[right - left + 1];
        // 合并两个排序数组
        int i = 0, j = 0, index = 0;
        while (i < mergeA.length && j < mergeB.length) {
            if (mergeA[i] <= mergeB[j]) {
                result[index] = mergeA[i++];
            } else {
                result[index] = mergeB[j++];
            }
            index++;
        }
        if (i < mergeA.length) {
            for (int k = i; k < mergeA.length; k++) {
                result[index++] = mergeA[i++];
            }
        } else {
            for (int k = j; k < mergeB.length; k++) {
                result[index++] = mergeB[j++];
            }
        }
        return result;
    }

    private int[] shellSort(int[] nums) {
        // 利用插入排序的思想，通过指数次的前置排序，让数据的顺序竟可能的排好，后续减少交换次数
        for (int step = nums.length / 2; step > 0; step /= 2) {
            for (int j = step; j < nums.length; j += step) {
                for (int i = j; i > 0; i -= step) {
                    if (nums[i] < nums[i - step]) {
                        swap(nums, i, i - step);
                    } else {
                        break;
                    }
                }
            }
        }
        return nums;
    }

    private int[] insertSort(int[] nums) {
        // 让前k个排好序，然后将k+1与前K个数进行比较排序
        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0; j--) {
                if (nums[j] < nums[j - 1]) {
                    swap(nums, j, j - 1);
                }
            }
        }

        return nums;
    }

    private int[] selectSort(int[] nums) {
        // 选择排序，从0到n-1选择各个位置进行定位，而后与后面的数据进行比较，记录最小的值，循环完后替换最小值到index

        for (int i = 0; i < nums.length - 1; i++) {
            int index = i;
            // 遍历比较
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[index]) {
                    index = j;
                }
            }
            if (index != i) {
                swap(nums, i, index);
            }
        }
        return nums;
    }


    private int[] bobbleSort(int[] list) {
        // 从底部向上部进行移动，移动n-1次,每比较一次，满足底部小于上一个，则替换
        int length = list.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = length - 1; j > i; j--) {
                if (list[j] < list[j - 1]) {
                    swap(list, j, j - 1);
                }
            }
        }

        return list;

    }

    private void swap(int[] list, int i, int j) {
        int temp = list[i];
        list[i] = list[j];
        list[j] = temp;
    }
}
