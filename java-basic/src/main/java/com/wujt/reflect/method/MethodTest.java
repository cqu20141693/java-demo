package com.wujt.reflect.method;

import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Method
 * <p>
 * API:
 * 获取方法返回值类型
 * 获取方法名称
 * 获取方法参数以及参数的类型和名称和参数注解
 * 获取方法注解
 * <p>
 * 反射调用静态方法： 第一个参数为null
 *
 * @author gow
 * @date 2022/1/18
 */
public class MethodTest {

    public static void main(String[] args) throws NoSuchMethodException {
        Class<MethodTest> aClass = MethodTest.class;
        System.out.println("class:" + aClass);
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            System.out.println("method:" + method);
            //得到该方法参数信息数组
            Parameter[] parameters = method.getParameters();
            //遍历参数数组，依次输出参数名和参数类型
            Arrays.stream(parameters).forEach(p -> {
                System.out.println("parameter:" + p.getName() + " : " + p.getType());
            });
        }
        Method testMethod = aClass.getMethod("test", String.class, Integer.class, Boolean.class);
        Parameter[] parameters = testMethod.getParameters();
        Type[] genericParameterTypes = testMethod.getGenericParameterTypes();
        AnnotatedType[] annotatedParameterTypes = testMethod.getAnnotatedParameterTypes();

        // 获取方法名
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(testMethod);
        System.out.println(Arrays.toString(parameterNames));
    }


    public void test(String name, Integer age, Boolean man) {
        System.out.println(name + age + man);
    }
}
