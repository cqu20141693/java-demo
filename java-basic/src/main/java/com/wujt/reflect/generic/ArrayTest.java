package com.wujt.reflect.generic;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Array类常用API:
 * get(Object array,int index):获取数组中指定位置的内容。
 * newInstance(Class<?> componenType,int length):根据指定类型和指定长度，开辟一个新的数组
 * set(Object array,int index,Object value):修改数组中指定位置的内容
 * 数组对象.getClass().getComponentType()获取到数组对应的Class对象。
 * 泛型数组
 * wcc 2022/5/6
 */
@Data
@Slf4j
public class ArrayTest {
    private People[] peoples;

    @SneakyThrows
    public static void main(String[] args) {
        ArrayTest arrayTest = new ArrayTest();
        Class<? extends ArrayTest> aClass = arrayTest.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isArray()) {
                Object newInstance = Array.newInstance(field.getType().getComponentType(), 2);
                Class<?> componentType = newInstance.getClass().getComponentType();
                log.info("componentType={}", componentType);


                Object instance = Array.newInstance(People.class, 2);

                field.set(arrayTest, instance);
            }
        }

    }
}
