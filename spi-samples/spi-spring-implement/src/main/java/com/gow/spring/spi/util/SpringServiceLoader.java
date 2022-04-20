package com.gow.spring.spi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gow
 * @date 2021/6/24
 */
@Slf4j
public class SpringServiceLoader {

    public static <T> T getSpringFactoriesInstances(Class<T> type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        List<T> list = SpringFactoriesLoader.loadFactories(type, classLoader);
        if (list == null && list.size() == 0) {
            return null;
        } else {
            AnnotationAwareOrderComparator.sort(list);
            return list.get(0);
        }

    }

    public static Set<? extends Class<?>> getSpringFactoriesClasses(Class<?> type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(type, classLoader);
        if (factoryNames == null && factoryNames.size() == 0) {
            return null;
        } else {
            Set<? extends Class<?>> classes = factoryNames.stream().map(name -> {
                try {
                    return Class.forName(name, true, classLoader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    log.info("spring factories exit error name={}", name);
                }
                return null;
            }).collect(Collectors.toSet());
            classes.remove(null);
            return classes;
        }
    }

}
