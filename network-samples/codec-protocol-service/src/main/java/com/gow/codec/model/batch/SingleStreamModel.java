package com.gow.codec.model.batch;

import lombok.Data;

/**
 * @author gow
 * @date 2021/7/29
 */
@Data
public class SingleStreamModel {

    private Long bizTime;
    // 通道名称
    private String stream;
    // 编码类型
    private Byte encodeType;
    // 数据
    private Object data;
}
