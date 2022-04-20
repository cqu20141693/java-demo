package com.wujt.reflect.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author gow
 * @date 2021/9/24
 */
public class Main extends SuperClass<User> {
    public static void main(String[] args) {
        // 获取 Main 的超类 SuperClass 的签名(携带泛型). 这里为: xxx.xxx.xxx.SuperClass<xxx.xxx.xxx.User>
        Type genericSuperclass = Main.class.getGenericSuperclass();
        // 强转成 参数化类型 实体.
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        System.out.println(parameterizedType);

        // 获取超类的泛型类型数组. 即SuperClass<User>的<>中的内容, 因为泛型可以有多个, 所以用数组表示
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type genericType = actualTypeArguments[0];
        System.out.println(genericType);
        Class<User> clazz = (Class<User>) genericType;

        SuperClass<User> userSuperClass = new SuperClass<>();
        Type superclass = userSuperClass.getClass().getGenericSuperclass();
        System.out.println(superclass);
    }
}
