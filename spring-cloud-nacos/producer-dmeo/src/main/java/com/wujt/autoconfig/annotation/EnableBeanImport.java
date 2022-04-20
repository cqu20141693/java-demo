package com.wujt.autoconfig.annotation;

import com.wujt.autoconfig.MyConfiguration;
import com.wujt.autoconfig.MyRegistrarConfiguration;
import com.wujt.autoconfig.MySelectorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wujt  2021/6/1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MyConfiguration.class, MyRegistrarConfiguration.class, MySelectorConfiguration.class})
public @interface EnableBeanImport {
}
