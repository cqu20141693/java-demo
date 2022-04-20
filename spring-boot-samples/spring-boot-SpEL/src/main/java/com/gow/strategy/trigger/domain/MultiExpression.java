package com.gow.strategy.trigger.domain;

import java.util.Set;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/3
 */
@Data
public class MultiExpression implements Comparable<MultiExpression> {

    private ConjunctionType type = ConjunctionType.OR;
    private Set<SingleExpression> simpleRules;
    private Integer order = 0;

    /**
     * 自然升序
     *
     * @param e 表达式
     */
    @Override
    public int compareTo(MultiExpression e) {
        return this.order - e.order;
    }
}
