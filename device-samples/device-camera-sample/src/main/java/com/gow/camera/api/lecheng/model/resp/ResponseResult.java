package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 */
@Data
public class ResponseResult<T> {
    private String id;
    private Result<T> result;
}
