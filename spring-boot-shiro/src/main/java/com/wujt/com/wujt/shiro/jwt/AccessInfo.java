package com.wujt.com.wujt.shiro.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: majiwei
 * @since: 2021/4/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessInfo {

    private String username;
    private String userKey;
}
