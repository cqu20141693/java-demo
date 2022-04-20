package com.gow.spi.core;

import java.lang.annotation.*;

/**
 * @author gow
 * @date 2021/6/25
 * Join
 * Adding this annotation to a class indicates joining the extension mechanism.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Join {
}
