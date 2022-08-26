package com.wcc.openapi.domain.app;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * wcc 2022/8/26
 */
@Data
@Accessors(chain = true)
public class AppInfo {

    private String appId;
    private String appSecret;
    private String descr;
}
