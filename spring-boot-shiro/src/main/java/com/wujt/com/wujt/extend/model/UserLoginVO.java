package com.wujt.com.wujt.extend.model;

import lombok.Data;

/**
 * @author wujt
 */
@Data
public class UserLoginVO {
    private String userKey;
    private String username;
    private String token;
}
