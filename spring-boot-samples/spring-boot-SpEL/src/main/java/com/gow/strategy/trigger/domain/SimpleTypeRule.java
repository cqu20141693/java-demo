package com.gow.strategy.trigger.domain;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/2
 */
@Data
public class SimpleTypeRule implements Comparable<SimpleTypeRule> {

    private String index;
    List<MultiExpression> expressions;
    private Integer order;

    /**
     * 自然升序
     *
     * @param rule 规则
     */
    @Override
    public int compareTo(SimpleTypeRule rule) {
        return this.order - rule.order;
    }
}