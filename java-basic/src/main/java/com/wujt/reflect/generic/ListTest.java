package com.wujt.reflect.generic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * wcc 2022/5/5
 */
@Slf4j
public class ListTest {

    public List<People> stringList = new ArrayList<>();

    public static void main(String[] args) {
        test();

    }

    @SneakyThrows
    private static void test() {
        ListTest listTest = new ListTest();
        Class<? extends ListTest> aClass = listTest.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (type == List.class) {
                // 当前集合的泛型类型
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    // 得到泛型里的class类型对象
                    Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                    List<Object> curEleList = new ArrayList<>();
                    Object actualType = actualTypeArgument.getDeclaredConstructor().newInstance();
                    curEleList.add(actualType);
                    field.set(listTest, curEleList);
                } else {
                    log.info("occur not support genericType");
                }
            }
        }
    }
}
