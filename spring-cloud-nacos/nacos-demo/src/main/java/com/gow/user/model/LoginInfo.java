package com.gow.user.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginInfo {
    private String username;
    private String password;
}
