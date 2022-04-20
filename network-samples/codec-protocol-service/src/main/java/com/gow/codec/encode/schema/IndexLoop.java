package com.gow.codec.encode.schema;

import lombok.Data;

/**
 * @author gow
 * @date 2021/9/22
 */
@Data
public class IndexLoop {
    // 长度不能超过127
    private byte[] name;
}
