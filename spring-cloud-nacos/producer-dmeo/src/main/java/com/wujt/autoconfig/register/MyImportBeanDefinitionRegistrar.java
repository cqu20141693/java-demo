package com.wujt.autoconfig.register;

import com.wujt.autoconfig.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wujt  2021/6/1
 */
@Slf4j
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("MyImportBeanDefinitionRegistrar invoked");
        Set<Class> classSet = getClasses();
        classSet.forEach(clazz -> {
            AnnotatedGenericBeanDefinition annotatedGenericBeanDefinition = new AnnotatedGenericBeanDefinition(clazz);
            registry.registerBeanDefinition(clazz.getName(), annotatedGenericBeanDefinition);
        });
        log.info("MyImportBeanDefinitionRegistrar register {} class", classSet.size());
    }

    private Set<Class> getClasses() {
        HashSet<Class> set = new HashSet<>();
        set.add(Apple.class);
        set.add(Banana.class);
        set.add(Cherry.class);
        set.add(Strawberry.class);
        set.add(Watermelon.class);
        return set;
    }
}
