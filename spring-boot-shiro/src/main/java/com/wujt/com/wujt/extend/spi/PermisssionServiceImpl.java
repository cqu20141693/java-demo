package com.wujt.com.wujt.extend.spi;

import com.wujt.com.wujt.shiro.spi.PermissionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wujt
 */
public class PermisssionServiceImpl implements PermissionService {
    @Override
    public Set<String> getPermissions(String userKey) {
        HashSet<String> set = new HashSet<>();
        set.add("add");
        set.add("select");
        return set;
    }
}
