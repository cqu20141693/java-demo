package com.wujt.com.wujt.extend.model;

import com.wujt.com.wujt.shiro.domain.SysUserInfo;
import lombok.Data;

/**
 * @author wujt
 */
@Data
public class UserInfo extends SysUserInfo {
    private Integer status;

    @Override
    public boolean valid() {
        return !status.equals(StatusEnum.DISABLE.intValue());
    }
}
