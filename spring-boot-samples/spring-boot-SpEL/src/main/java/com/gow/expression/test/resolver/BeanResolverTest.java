package com.gow.expression.test.resolver;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/21 0021
 */
@Component
public class BeanResolverTest implements BeanFactoryAware, SmartInitializingSingleton {

    private BeanFactory beanFactory;

    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();
    private BeanExpressionContext expressionContext;

    private String uuidEL = "${pulsar.uuid}";
    private String valueEL = "${pulsar.name}";
    private String randomEL = "${pulsar.random}";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory, null);

        }
    }


    @Override
    public void afterSingletonsInstantiated() {
        Object expression = resolveExpression(uuidEL);
        if (expression instanceof String) {
            System.out.println("uuid={}" + expression);
        }
        Object resolveExpression = resolveExpression(valueEL);
        if (resolveExpression instanceof String) {
            System.out.println("value={}" + resolveExpression);
        }
        Object random = resolveExpression(randomEL);
        if (random instanceof String) {
            System.out.println("value={}" + random);
        }
    }

    private Object resolveExpression(String value) {
        return this.resolver.evaluate(this.resolve(value), this.expressionContext);
    }

    private String resolve(String value) {
        return this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory ?
                ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value) : value;
    }

}
