package com.gow.spring.http;

import com.gow.common.AuthContext;

/**
 * 在参数解析前将对象设置到请求wrapper中，可以是filter 或 interceptor
 *
 * @author wujt  2021/5/24
 */
public class RequestAuthContextResolver extends RequestArgumentResolver {

    @Override
    public Class<AuthContext> getArgumentType() {
        return AuthContext.class;
    }

}
