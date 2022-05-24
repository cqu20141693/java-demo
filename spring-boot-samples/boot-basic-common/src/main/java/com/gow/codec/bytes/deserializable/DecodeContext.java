package com.gow.codec.bytes.deserializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 解码上下文
 * wcc 2022/5/5
 */
@Data
@Builder
@AllArgsConstructor
public class DecodeContext {
    private Object obj;
    private int offset;
    private Boolean success;

    public boolean success() {
        return success == null || success;
    }

}
