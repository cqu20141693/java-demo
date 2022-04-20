package com.gow.pulsar.core.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PulsarSubscribeConfigurationSelector.class})
public @interface EnablePulsar {
}
