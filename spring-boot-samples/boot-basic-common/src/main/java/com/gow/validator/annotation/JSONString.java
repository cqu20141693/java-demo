package com.gow.validator.annotation;


import com.alibaba.fastjson.JSONObject;
import com.gow.validator.JSONStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wujt  2021/5/18
 * 注解只能使用在body中，url中不能发送json String
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {JSONStringValidator.class})
public @interface JSONString {
    // 指定json转换的类，默认为JSONObject
    Class jsonClass() default JSONObject.class;

    // 默认错误消息
    String message() default "JSON String格式错误";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};
}
