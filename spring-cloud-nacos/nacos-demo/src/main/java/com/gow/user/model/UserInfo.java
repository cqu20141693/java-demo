package com.gow.user.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfo {
    private String userKey;
    private String username;
    private String password;
}
