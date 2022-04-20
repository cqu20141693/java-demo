package com.gow.common;

/**
 * @author wujt  2021/5/18
 * 如果需要自定义错误编码，需要继承ReturnCode接口
 */
public interface ReturnCode {

    /**
     * 获取返回编码
     * format: d{4}d{6}
     *
     * @return int
     * @date 2021/5/18 16:54
     */
    String code();

    /**
     * 获取返回描述信息
     *
     * @return java.lang.String
     * @date 2021/5/18 16:54
     */
    String message();
}
