package com.gow.spring.request;

import com.gow.spring.session.HttpSessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.constraints.NotNull;

/**
 * wcc 2022/7/27
 */
@Configuration
public class SpringConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean<RequestFilter> registerRequestFilter() {
        FilterRegistrationBean<RequestFilter> bean = new FilterRegistrationBean<>();
        bean.setOrder(0);
        bean.setFilter(new RequestFilter());
        bean.setName("RequestFilter");
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HttpSessionFilter> registerSessionFilter() {
        FilterRegistrationBean<HttpSessionFilter> bean = new FilterRegistrationBean<>();
        bean.setOrder(0);
        bean.setFilter(new HttpSessionFilter());
        bean.setName("HttpSessionFilter");
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCompleteInterceptor());
    }
}
