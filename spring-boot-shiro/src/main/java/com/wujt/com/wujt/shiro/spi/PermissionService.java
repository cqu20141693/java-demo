package com.wujt.com.wujt.shiro.spi;

import java.util.Set;

/**
 * @author wujt
 */
public interface PermissionService {

    Set<String> getPermissions(String userKey);
}
