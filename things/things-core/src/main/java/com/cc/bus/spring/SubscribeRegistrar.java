package com.cc.bus.spring;

import com.cc.bus.event.EventBus;
import com.cc.bus.event.TopicSubscribe;
import com.cc.bus.event.Subscription;
import com.cc.bus.topic.TopicUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * spring broker parse Subscribe
 * wcc 2022/6/9
 *
 * @see TopicSubscribe
 */
@Component
@Slf4j
@AllArgsConstructor
public class SubscribeRegistrar implements BeanPostProcessor, Ordered {
    private final EventBus eventBus;

    private final Environment environment;

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        Class<?> type = ClassUtils.getUserClass(bean);
        ReflectionUtils.doWithMethods(type, method -> {
            AnnotationAttributes subscribes = AnnotatedElementUtils.getMergedAnnotationAttributes(method, TopicSubscribe.class);
            if (CollectionUtils.isEmpty(subscribes)) {
                return;
            }
            String id = subscribes.getString("id");
            if (!StringUtils.hasText(id)) {
                id = type.getSimpleName().concat(".").concat(method.getName());
            }

            Subscription subscription = Subscription
                    .builder()
                    .subscriberId("spring:" + id)
                    .topics(Arrays.stream(subscribes.getStringArray("value"))
                            .map(this::convertTopic)
                            .flatMap(topic -> TopicUtils.expand(topic).stream())
                            .collect(Collectors.toList())
                    )
                    .features((Subscription.Feature[]) subscribes.get("features"))
                    .build();

            eventBus.subscribe(subscription);
        });

        return bean;
    }

    protected String convertTopic(String topic) {
        if (!topic.contains("${")) {
            return topic;
        }
        return TemplateParser.parse(topic, template -> {
            String[] arr = template.split(":", 2);
            String property = environment.getProperty(arr[0], arr.length > 1 ? arr[1] : "");
            if (StringUtils.isEmpty(property)) {
                throw new IllegalArgumentException("Parse topic [" + template + "] error, can not get property : " + arr[0]);
            }
            return property;
        });
    }

    @Override
    public int getOrder() {
        // 一定要注意 broker bean 加载时间需要前于使用@Subscribe bean
        return Ordered.LOWEST_PRECEDENCE;
    }
}
