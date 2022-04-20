package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 */
@Data
public class Result<T> {
    // 0: 表示成功
    private String code;
    private String msg;
    private T data;
}
