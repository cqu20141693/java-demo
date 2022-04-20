package com.wujt.backend.domain.authenticate;

import lombok.Data;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@Data
public class UserAuthReq {

    private TokenType tokenType;

    private String userKey;

    private String password;
}
