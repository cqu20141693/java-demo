package com.gow.spring.http;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * @author wujt  2021/5/24
 */
public class BufferedTranslateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapepr = null;
        if (request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            Optional.ofNullable(contentType).ifPresent(type -> {
                if (contentType.contains("multipart/form-data")) {
                    try {
                        ((HttpServletRequest) request).getParts();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                } else if (contentType.contains("application/x-www-form-urlencoded")) {
                    request.getParameterMap();
                }
            });
            // wrapper
            requestWrapepr = new BufferedServletRequestWrapper((HttpServletRequest) request);
        }
        if (requestWrapepr == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapepr, response);
        }
    }

    @Override
    public void destroy() {

    }
}
