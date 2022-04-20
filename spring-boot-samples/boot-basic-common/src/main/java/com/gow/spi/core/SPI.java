package com.gow.spi.core;

import java.lang.annotation.*;

/**
 * @author gow
 * @date 2021/6/25
 * SPI Extend the processing.
 * All spi system reference the apache implementation of
 * https://github.com/apache/dubbo/blob/master/dubbo-common/src/main/java/org/apache/dubbo/common/extension.
 * @see ExtensionFactory
 * @see ExtensionLoader
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
