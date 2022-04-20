package com.wujt.autoconfig;

import com.wujt.autoconfig.annotation.EnableBeanImport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt  2021/6/1
 */
@Configuration
@EnableBeanImport
public class MyAutoConfiguration {
}
