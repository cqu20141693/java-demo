package com.wujt.排序;

import java.util.*;

/**
 * 线性排序是以空间换时间的思想进行的；
 *
 * @author wujt
 */
public class LinearSort {
    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            integers.add(random.nextInt(1000));
        }
        Collections.sort(integers);
        System.out.println(integers);
    }

    /**
     * 计数排序核心思想： 通过桶进行计数；之后遍历桶实现排序
     * <p>
     * <p>
     * 计数排序，a是数组，n是数组大小。假设数组中存储的都是非负整数。
     * 计数排序只能用在数据范围不大的场景中，如果数据范围 k 比要排序的数据 n 大很多，
     * 就不适合用计数排序了。而且，计数排序只能给非负整数排序，如果要排序的数据是其他类型的，
     * 要将其在不改变相对大小的情况下，转化为非负整数
     */
    public static void countingSort(List<Integer> list, int n) {
        if (n <= 1) return;

        // 查找数组中数据的范围
        int max = list.get(0);
        for (int i = 1; i < n; ++i) {
            if (max < list.get(i)) {
                max = list.get(i);
            }
        }

        int[] c = new int[max + 1]; // 申请一个计数数组c，下标大小[0,max]
        for (int i = 0; i <= max; ++i) {
            c[i] = 0;
        }

        // 计算每个元素的个数，放入c中
        for (int i = 0; i < n; ++i) {
            c[list.get(i)]++;
        }

        // 依次累加
        // 计算每一个桶的数据的最大索引位
        for (int i = 1; i <= max; ++i) {
            c[i] = c[i - 1] + c[i];
        }

        // 临时数组r，存储排序之后的结果
        int[] r = new int[n];
        // 计算排序的关键步骤，有点难理解
        // 从后往前将访问的数据放入对应当前最大的index位置
        for (int i = n - 1; i >= 0; --i) {
            int index = c[list.get(i)] - 1;

            r[index] = list.get(i);
            // 动态维护当前的最大的索引位置；
            c[list.get(i)]--;
        }

        // 将结果拷贝给a数组
        for (int i = 0; i < n; ++i) {
            list.set(i, r[i]);
        }
    }

    /**
     * 花O(n)的时间扫描一下整个序列 A，获取最小值 min 和最大值 max
     * 开辟一块新的空间创建新的数组 B，长度为 ( max - min + 1)
     * 数组 B 中 index 的元素记录的值是 A 中某元素出现的次数
     * 最后输出目标整数序列，具体的逻辑是遍历数组 B，输出相应元素以及对应的个数
     *
     * @param sourceArray
     * @return
     */
    public int[] countingSort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        int maxValue = getMaxValue(arr);
        return countingSort(arr, maxValue);
    }

    private int[] countingSort(int[] arr, int maxValue) {
        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : arr) {
            bucket[value]++;
        }
        // 记录当前排序的位置
        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                // 存在一个统计；就将当前位置数放到索引位置；直到当前桶统计的数据为0
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    private int getMaxValue(int[] arr) {
        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }


    /**
     * 核心思想是将要排序的数据分到几个有序的桶里，每个桶里的数据再单独进行排序。
     * 桶内排完序之后，再把每个桶里的数据按照顺序依次取出，组成的序列就是有序的了
     * <p>
     * 其使用的场景： 数据量少，具有可控范围；
     * 其思想可以应用到基数排序和计数排序中；
     *
     * @param list
     */
    public static void bucketSort(List<Integer> list) {

    }

    /**
     * 基数排序：通过多次的桶排序实现数据的排序
     *
     * @param
     */
    public int[] radixSort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        int maxDigit = getMaxDigit(arr);
        return radixSort(arr, maxDigit);
    }

    /**
     * 获取最高位数
     */
    private int getMaxDigit(int[] arr) {
        int maxValue = getMaxValue(arr);
        return getNumLenght(maxValue);
    }

    protected int getNumLenght(long num) {
        if (num == 0) {
            return 1;
        }
        int lenght = 0;
        for (long temp = num; temp != 0; temp /= 10) {
            lenght++;
        }
        return lenght;
    }

    private int[] radixSort(int[] arr, int maxDigit) {
        int mod = 10;
        int dev = 1;

        for (int i = 0; i < maxDigit; i++, dev *= 10, mod *= 10) {
            // 考虑负数的情况，这里扩展一倍队列数，其中 [0-9]对应负数，[10-19]对应正数 (bucket + 10)
            int[][] counter = new int[mod * 2][0];

            for (int j = 0; j < arr.length; j++) {
                int bucket = ((arr[j] % mod) / dev) + mod;
                counter[bucket] = arrayAppend(counter[bucket], arr[j]);
            }

            int pos = 0;
            // 对每一次基数桶构建完成后进行重新排序数组；
            for (int[] bucket : counter) {
                for (int value : bucket) {
                    arr[pos++] = value;
                }
            }
        }

        return arr;
    }

    /**
     * 数组扩容函数
     */
    private int[] arrayAppend(int[] arr, int value) {
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = value;
        return arr;
    }

}
