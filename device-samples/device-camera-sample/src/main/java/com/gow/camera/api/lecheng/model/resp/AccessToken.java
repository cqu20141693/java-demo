package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 * accessToken 在过期之前请求返回结果一致
 */
@Data
public class AccessToken {
    private String accessToken;
    private Long expireTime;
}
