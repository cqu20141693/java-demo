package com.cc.bus.event;

import com.cc.bus.spring.SubscribeRegistrar;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @see EventBus
 * @see SubscribeRegistrar
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TopicSubscribe {

    /**
     * 要订阅的topic,topic是树结构,
     * 和{@link org.springframework.util.AntPathMatcher}类似,支持通配符: **表示多层目录,*表示单层目录.
     * <ul>
     *     <li>
     *        /device/p1/d1/online
     *     </li>
     *     <li>
     *       /device/p1/d1,d2/online
     *     </li>
     *     <li>
     *        /device/p1/&#42;/online
     *     </li>
     *     <li>
     *       /device/&#42;&#42;
     *    </li>
     * </ul>
     * <p>
     * 支持使用表达式
     * <pre>
     * /device/${sub.product-id}/**
     * </pre>
     *
     * @return topics
     * @see TopicSubscribe#value()
     * @see EventBus#subscribe(Subscription)
     */
    @AliasFor("value")
    String[] topics() default {};

    /**
     * @return topics
     * @see TopicSubscribe#topics()
     */
    @AliasFor("topics")
    String[] value() default {};
}
