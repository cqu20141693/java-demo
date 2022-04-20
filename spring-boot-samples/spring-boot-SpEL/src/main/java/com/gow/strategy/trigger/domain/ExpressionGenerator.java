package com.gow.strategy.trigger.domain;

import com.gow.codec.model.EncodeTypeEnum;
import org.springframework.expression.Expression;

/**
 * @author gow
 * @date 2021/9/2
 */
public interface ExpressionGenerator {

    Expression generate(EncodeTypeEnum encodeType);

}
