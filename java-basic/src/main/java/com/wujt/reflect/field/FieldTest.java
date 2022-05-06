package com.wujt.reflect.field;

import lombok.Data;

/**
 * 反射 Field API
 * java.lang.reflect.Field
 * 1. 获取当前对象的成员变量的类型
 * 2. 重新设值
 * 3. 获取注解
 * <p>
 * Class 类获取Field：
 * getFields()只能获取public的字段，包括父类的。
 * getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，privat
 * wcc 2022/5/5
 */
@Data
public class FieldTest {
}
