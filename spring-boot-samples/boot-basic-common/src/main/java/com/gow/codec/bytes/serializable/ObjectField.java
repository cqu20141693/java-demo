package com.gow.codec.bytes.serializable;

import com.gow.codec.bytes.DataType;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Target({FIELD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ObjectField {

    // 数据类型
    DataType dataType() default DataType.OBJECT;

    // 动态根据属性获取属性的类型,此方法为无参方法,使用场景为对象属性动态由其他属性决定
    String classMethod() default "";
    // List 集合数据的长度字段
    String loopFieldName() default "";
    // String:动态字符串长度属性
    int length() default 0;

    DataType enumType() default DataType.STRING;

    String enumConverter() default "valueOf";
}
