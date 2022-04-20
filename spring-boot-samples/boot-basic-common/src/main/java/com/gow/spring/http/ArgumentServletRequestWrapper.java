package com.gow.spring.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author wujt  2021/5/24
 */
public class ArgumentServletRequestWrapper<T> extends HttpServletRequestWrapper {
    private T argument;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public ArgumentServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public T getArgument() {
        return argument;
    }

    public void setArgument(T argument) {
        this.argument = argument;
    }

    public Class getArgumentType() {
        return argument.getClass();
    }
}
