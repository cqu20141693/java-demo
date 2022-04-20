package com.gow.strategy.trigger.domain;

import com.gow.codec.model.EncodeTypeEnum;
import java.util.List;
import java.util.Set;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author gow
 * @date 2021/9/2
 */
public abstract class BaseRuleExpressionGenerator implements ExpressionGenerator {

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    public static Expression createSimpleRuleExpression(SimpleTypeRule triggerRuleModel, Integer effectiveTimeStart,
                                                        Integer effectiveTimeEnd) {

        List<MultiExpression> expressions = triggerRuleModel.getExpressions();
        expressions.sort(null);
        StringBuilder builder = new StringBuilder();
        int multiCounter = 0, multiLength = expressions.size();
        for (MultiExpression expression : expressions) {
            Set<SingleExpression> simpleRules = expression.getSimpleRules();
            int counter = 0, length = simpleRules.size();
            builder.append("(");
            for (SingleExpression single : simpleRules) {
                builder.append(triggerRuleModel.getIndex()).append(" ");
                builder.append(single.getOperationType().getCode()).append(" ");
                switch (EncodeTypeEnum.parseFromType(single.getValueType())) {
                    case TYPE_INT:
                    case TYPE_LONG:
                    case TYPE_FLOAT:
                    case TYPE_DOUBLE:
                        builder.append(single.getValue());
                        break;
                    case TYPE_STRING:
                        builder.append("'").append(single.getValue()).append("'");
                        break;
                    default:
                        return null;
                }
                if (++counter != length) {
                    builder.append(" and ");
                }

            }
            builder.append(")");
            if (++multiCounter != multiLength) {
                builder.append(" ").append(expression.getType().getCode()).append(" ");
            }
        }

        if (effectiveTimeStart != null) {
            builder.append(" and #time >= ").append(effectiveTimeStart);
        }
        if (effectiveTimeEnd != null) {
            builder.append(" and #time < ").append(effectiveTimeEnd);
        }

        return parser.parseExpression(builder.toString());
    }

    public static Expression createJsonRuleExpression(SimpleTypeRule triggerRuleModel, Integer effectiveTimeStart,
                                                      Integer effectiveTimeEnd) {
        StringBuilder builder = new StringBuilder();

        //


        return parser.parseExpression(builder.toString());
    }
}
