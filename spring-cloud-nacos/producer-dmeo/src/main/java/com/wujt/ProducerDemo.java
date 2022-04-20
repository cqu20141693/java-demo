package com.wujt;

import com.wujt.autoconfig.MyAutoConfiguration;
import com.wujt.autoconfig.MyConfiguration;
import com.wujt.autoconfig.MyRegistrarConfiguration;
import com.wujt.autoconfig.MySelectorConfiguration;
import com.wujt.autoconfig.domain.Apple;
import com.wujt.autoconfig.domain.HuaWei;
import com.wujt.autoconfig.domain.Watermelon;
import com.wujt.event.MySelfEvent;
import com.wujt.event.MySelfEventListener;
import com.wujt.factory.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wujt
 */
@SpringBootApplication(scanBasePackages = {"com.wujt","com.gow"})
@EnableDiscoveryClient
@Slf4j
public class ProducerDemo {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProducerDemo.class, args);
        applicationContext.addApplicationListener(new MySelfEventListener());
        log.info("spring application started");
        applicationContext.publishEvent(new MySelfEvent(applicationContext));
        ProducerDemo bean = applicationContext.getBean(ProducerDemo.class);


        testAutoConfig(applicationContext);
        testImportConfig(applicationContext);
        testImportBean(applicationContext);
        testFactoryBean(applicationContext);
    }

    private static void testFactoryBean(ConfigurableApplicationContext applicationContext) {
        Object car = applicationContext.getBean("carFactoryBean");
        Object carFactoryBean = applicationContext.getBean("&carFactoryBean");
        Object car1 = applicationContext.getBean(Car.class);
        log.info("car={},carFactoryBean={},car1={}", car, carFactoryBean, car1);
    }

    private static void testImportBean(ConfigurableApplicationContext applicationContext) {
        Apple apple = applicationContext.getBean(Apple.class);
        Watermelon watermelon = applicationContext.getBean(Watermelon.class);
        HuaWei huaWei = applicationContext.getBean(HuaWei.class);
        log.info("apple={},watermelon={}，huaWei={}", apple, watermelon, huaWei);
    }

    private static void testAutoConfig(ConfigurableApplicationContext applicationContext) {
        MyAutoConfiguration myConfiguration = applicationContext.getBean(MyAutoConfiguration.class);

        log.info("class={}", myConfiguration);
    }

    private static void testImportConfig(ConfigurableApplicationContext applicationContext) {
        MyConfiguration myConfiguration = applicationContext.getBean(MyConfiguration.class);
        MyRegistrarConfiguration myRegistrarConfiguration = null;
        MySelectorConfiguration mySelectorConfiguration = null;
        try {
            myRegistrarConfiguration = applicationContext.getBean(MyRegistrarConfiguration.class);
            mySelectorConfiguration = applicationContext.getBean(MySelectorConfiguration.class);
        } catch (NoSuchBeanDefinitionException exception) {
            if (exception.getBeanType() == MyRegistrarConfiguration.class) {
                mySelectorConfiguration = applicationContext.getBean(MySelectorConfiguration.class);
            }
            exception.printStackTrace();
        }

        log.info("class={},retister={},selector={}", myConfiguration, myRegistrarConfiguration, mySelectorConfiguration);
    }
}
