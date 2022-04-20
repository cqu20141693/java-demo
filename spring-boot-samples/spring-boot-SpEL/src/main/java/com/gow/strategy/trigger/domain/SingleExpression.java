package com.gow.strategy.trigger.domain;

import lombok.Data;

/**
 * @author gow
 * @date 2021/9/3
 */
@Data
public class SingleExpression {

    private OperationType operationType;
    private String valueType;
    private String value;
}
