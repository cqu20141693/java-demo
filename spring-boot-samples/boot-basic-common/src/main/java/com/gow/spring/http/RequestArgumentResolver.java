package com.gow.spring.http;

import com.gow.common.AuthContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 在参数解析前将对象设置到请求wrapper中，可以是filter 或 interceptor
 *
 * @author wujt  2021/5/24
 */
public abstract class RequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<AuthContext> aClass = getArgumentType();
        return parameter.getParameterType() == aClass;
    }

    /**
     * get request parameter parsing object type
     *
     * @return java.lang.Class<com.gow.common.AuthContext>
     * @date 2021/5/24 14:26
     */
    public abstract Class getArgumentType();

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequestWrapper request = webRequest.getNativeRequest(HttpServletRequestWrapper.class);
        if (request == null) {
            return null;
        }
        while (true) {
            if (request instanceof ArgumentServletRequestWrapper) {
                ArgumentServletRequestWrapper wrapper = (ArgumentServletRequestWrapper) request;
                if (wrapper.getArgumentType() == parameter.getParameterType()) {
                    return wrapper.getArgument();
                }
            } else {
                ServletRequest servletRequest = request.getRequest();
                if (!(servletRequest instanceof HttpServletRequestWrapper)) {
                    break;
                } else {
                    request = (HttpServletRequestWrapper) servletRequest;
                }
            }
        }

        return null;
    }
}
