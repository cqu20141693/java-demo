package com.wujt.排序;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class NonlinearSort {
    public static ThreadLocal<List<Integer>> integers = new ThreadLocal() {
        @Override
        protected List<Integer> initialValue() {
            List<Integer> integers = new ArrayList<>(10);
            integers.add(10);
            integers.add(9);
            integers.add(7);
            integers.add(8);
            integers.add(6);
            integers.add(4);
            integers.add(5);
            integers.add(1);
            integers.add(2);
            integers.add(3);
            integers.add(6);
            return integers;
        }
    };

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.DiscardPolicy());
        executor.submit(() -> bobbleSort(integers.get()));
        executor.submit(() -> selectSort(integers.get()));
        executor.submit(() -> shellSort(integers.get()));
        executor.submit(() -> insertSort(integers.get()));
        executor.submit(() -> quickSort(integers.get()));

        executor.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("After merge sort: " + mergeSort(integers.get()));
        });
    }

    /**
     * 冒泡排序
     * 思想: 每次循环都进行全部比较;这样就能定位到每个数据的位置;
     * 每次比较后发现满足都会swap
     * 时间复杂度: n^2 ,常数级空间复杂度
     * 实现思路 一级循环需要0:n-1;
     * 二级循环从0开始相邻对比;如果满足条件替换;
     *
     * @param list
     */
    public static List<Integer> bobbleSort(List<Integer> list) {

        System.out.println("Before bobble sort: " + list);
        if (list != null && list.size() > 1) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 0; j < list.size() - i - 1; j++) {
                    if (list.get(j) > list.get(j + 1)) {
                        swap(list, j, j + 1);
                    }
                }
            }
        }
        System.out.println("After bobble sort: " + list);
        return list;

    }

    /**
     * 选择排序
     * 思想: 定一个位置i,然后找到第i大的数的位置,然后进行交换;循环完成再swap
     * 时间复杂度为n^2,常数级空间复杂度
     * 二级循环: 第一级循环0:n-1:
     * 第二级循环i:n-1 :将i 于i:n-1进行对比,满足将位置记住,
     *
     * @param list
     * @return
     */
    public static List<Integer> selectSort(List<Integer> list) {
        System.out.println("Before select sort: " + list);
        if (list != null && list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                int temp = list.get(i);
                int index = i;
                for (int j = i + 1; j < list.size(); j++) {
                    if (temp > list.get(j)) {
                        temp = list.get(j);
                        index = j;
                    }
                }
                // swap
                swap(list, i, index);
                //list.set(i, temp);
            }
        }
        System.out.println("After select sort: " + list);
        return list;
    }

    /**
     * 插入排序
     * 思想: 将前m个进行排序
     *
     * @param list
     * @return
     */
    public static List<Integer> insertSort(List<Integer> list) {
        System.out.println("Before insert sort: " + list);
        if (list != null && list.size() > 1) {
            // 从下标为1的元素开始选择合适的位置插入，因为下标为0的只有一个元素，默认是有序的
            for (int i = 1; i < list.size(); i++) {

                // 记录要插入的数据
                int tmp = list.get(i);

                // 从已经排序的序列最右边的开始比较，找到比其小的数
                int j = i;
                while (j > 0 && tmp < list.get(j - 1)) {
                    list.set(j, list.get(j - 1));
                    j--;
                }
                // 存在比其小的数，插入
                if (j != i) {
                    list.set(j, tmp);
                }
            }
        }
        System.out.println("After insert sort: " + list);
        return list;
    }

    /**
     * 希尔排序 : 一种插入排序的升级版本
     * 首先是通过少量的操作尽量让数组有序;这样后面就会减少操作时间;
     *
     * @param list
     * @return
     */
    public static List<Integer> shellSort(List<Integer> list) {
        System.out.println("Before shell sort: " + list);
        Optional.ofNullable(list).filter(l -> l.size() > 1).map(ls -> {
            int middle;
            for (int gap = list.size() / 2; gap > 0; gap = gap / 2) {
                // 注意：这里和动图演示的不一样，动图是分组执行，实际操作是多个分组交替执行
                for (int i = gap; i < list.size(); i++) {
                    int j = i;
                    int current = list.get(i);
                    // 注意这里的思想 用来比较的位置会越来越小,而将大的数据放到大的位置
                    while (j - gap >= 0 && current < list.get(j - gap)) {
                        list.set(j, list.get(j - gap));
                        j = j - gap;
                    }
                    list.set(j, current);
                }
            }
            return true;
        });
        System.out.println("After shell sort: " + list);
        return list;
    }


    /**
     * 归并排序
     * 思想:
     * 如果子串是有序的,那个可以通过将分成的子串进行比较合并为有序的串
     * 而当子串都只有一个元素的时候自然有序;所以可以分解到最基础的;从而解决上面的排序
     * <p>
     * 分析方法的递归性: 当有n长度的数组时.2logn 次递归, 对于堆栈的使用不是很大;所以对于大数据量改排序的效果也很不错;
     * <p>
     * 当递归到size为1时;直接返回数据;
     * 接着就会对两个拆分的两个size进行合并;直到合并到最上层
     * <p>
     * 优化： 可以用多级划分；多个数组合并
     *
     * @param list
     * @return
     */
    public static List<Integer> mergeSort(List<Integer> list) {
        System.out.println("\n\nBefore merge sort: " + list);
        if (list.size() > 1) {

            int middle = list.size() / 2;

            List<Integer> left = new ArrayList();
            List<Integer> right = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                if (i < middle) {
                    left.add(list.get(i));
                } else {
                    right.add(list.get(i));
                }
            }
            // 先进行数组二分；然后再进行数组合并；
            // 直到数组的size==1 时 ；开始进行两个长度为1的数据合并
            list = merge(mergeSort(left), mergeSort(right));
        }
        System.out.println("After sub merge sort: " + list);
        //返回合并排序后的数组数据
        return list;
    }

    /**
     * 合并的必须是有序的数组
     *
     * @param left
     * @param right
     * @return
     */
    public static List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> list = new ArrayList<>(left.size() + right.size());
        // 当两个数组都存在数据时，比较add
        while (left.size() > 0 && right.size() > 0) {
            if (left.get(0) <= right.get(0)) {
                list.add(left.get(0));
                left.remove(0);
            } else {
                list.add(right.get(0));
                right.remove(0);
            }
        }
        // 存在一个数组不存在数据时
        while (right.size() > 0) {
            list.add(right.get(0));
            right.remove(0);
        }
        while (left.size() > 0) {
            list.add(left.get(0));
            left.remove(0);
        }
        return list;
    }

    /**
     * @param list
     * @return
     */
    public static List<Integer> quickSort(List<Integer> list) {
        System.out.println("Before quick sort: " + list);

        List<Integer> list1 = quickSorts(list, 0, list.size() - 1);
        System.out.println("After quick sort: " + list);
        return list1;
    }

    /**
     * 快速排序:
     * 思想:
     * 1. 通过第一个元素进行定位 position;定位左边的数一定比之小,右边的数一定比之大;
     * 2. 通过position 将数组分解为两个小数组和position
     * 3. 递归对两个小数组进行1,2,3 操作;
     * 4. 最后实现排序;
     *
     * @param list
     * @param left
     * @param right
     * @return
     */
    public static List<Integer> quickSorts(List<Integer> list, Integer left, Integer right) {
        //当拆分数组的size>1;需要进行定位快排
        if (left < right) {
            int partitionIndex = partition(list, left, right);
            // 根据之前的定位进行二分定位剩余的数据位置
            quickSorts(list, left, partitionIndex - 1);
            quickSorts(list, partitionIndex + 1, right);

        }
        // 当size=1 时 返回数据;
        return list;
    }

    /**
     * 定位
     *
     * @param list
     * @param left
     * @param right
     * @return
     */
    public static int partition(List<Integer> list, int left, int right) {
        // 设定基准值（pivot）左边
        int pivot = left;
        // 当前可以替换的位置
        int index = pivot + 1;
        // 循环基数后面的数据，如果存在小于基数的，就将
        for (int i = index; i <= right; i++) {
            // 比基准值小
            if (list.get(i) < list.get(pivot)) {
                //将比当前元素小的放到其右侧
                swap(list, i, index);
                //定位最后一个比当前值小的index
                index++;
            }
        }
        // 返回当前分区的位置
        if (pivot == index - 1) {
            return pivot;
        }
        // 将固定的值交换到最后一个比之小的值的位置;
        swap(list, pivot, index - 1);
        return index - 1;
    }

    public static void swap(List<Integer> list, int i, int j) {
        if (i != j) {
            int temp = list.get(j);
            list.set(j, list.get(i));
            list.set(i, temp);
        }
    }

    /**
     * 堆排序，首先实现堆的数据结构，java中优先级队列
     *
     * @param list
     * @return
     */
    public static List<Integer> heapSort(List<Integer> list) {

        // 默认使用的是类的Comparable接口，实现的是小顶堆
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(list.size());
        for (Integer data : list) {
            //
            priorityQueue.offer(data);
        }
        list.clear();
        for (int i = 0; i < priorityQueue.size(); i++) {
            list.add(priorityQueue.poll());
        }
        return list;
    }

}
