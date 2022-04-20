package com.wujt.array;

/**
 * @author wujt
 */
public class MaxArea {

    public int maxArea(int[] height) {
        int res = 0;
        for(int i = 0;i < height.length;i++){
            for(int j = i+1;j < height.length;j++){
                int temp = Math.min(height[i],height[j]) * (j-i);
                res = Math.max(res,temp);
            }
        }
        return res;
    }
}
