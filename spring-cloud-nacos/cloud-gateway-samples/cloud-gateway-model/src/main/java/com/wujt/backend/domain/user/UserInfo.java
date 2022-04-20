package com.wujt.backend.domain.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@Data
@Accessors(chain = true)
public class UserInfo {
    private String userKey;
    private String username;
    private String role;
    private String permission;
}
