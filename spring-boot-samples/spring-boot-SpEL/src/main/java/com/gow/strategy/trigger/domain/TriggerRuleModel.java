package com.gow.strategy.trigger.domain;

import static com.gow.strategy.trigger.domain.OperationType.UNKNOWN;
import static com.gow.strategy.trigger.domain.TriggerConstant.MAX_EFFECTIVE_TIME;
import static com.gow.strategy.trigger.domain.TriggerConstant.MIN_EFFECTIVE_TIME;
import static com.gow.strategy.trigger.domain.TriggerConstant.SIMPLE_INDEX;
import com.alibaba.fastjson.JSONObject;
import com.gow.codec.model.EncodeTypeEnum;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.expression.Expression;

/**
 * @author gow
 * @date 2021/9/2
 */
@Data
public class TriggerRuleModel extends BaseRuleExpressionGenerator {
    private TriggerRuleType type;
    private Integer effectiveTimeStart;
    private Integer effectiveTimeEnd;

    /**
     * {@link SimpleTypeRule } {@link JsonRule}
     */

    private JSONObject rule;

    public TriggerRuleModel() {
        this.effectiveTimeStart = MIN_EFFECTIVE_TIME;
        this.effectiveTimeEnd = MAX_EFFECTIVE_TIME;
    }

    @Override
    public Expression generate(EncodeTypeEnum encodeType) {
        if (type == null) {
            return null;
        }
        if (effectiveTimeStart < MIN_EFFECTIVE_TIME) {
            return null;
        }
        if (effectiveTimeEnd > MAX_EFFECTIVE_TIME) {
            return null;
        }
        {
            switch (type) {
                case SIMPLE:

                    SimpleTypeRule rule = JSONObject.toJavaObject(this.rule, SimpleTypeRule.class);
                    if (rule == null) {
                        return null;
                    }
                    if (!SIMPLE_INDEX.equals(rule.getIndex())) {
                        return null;
                    }
                    List<MultiExpression> multiExpressions = rule.getExpressions();

                    if (multiExpressions == null || multiExpressions.size() == 0) {
                        return null;
                    }
                    for (MultiExpression multiExpression : multiExpressions) {
                        if (multiExpression.getType() == null) {
                            return null;
                        }
                        Set<SingleExpression> expressions = multiExpression.getSimpleRules();
                        if (expressions == null || expressions.size() == 0) {
                            return null;
                        }
                        for (SingleExpression expression : expressions) {
                            if (!checkExpression(encodeType, expression)) {
                                return null;
                            }
                        }
                    }
                    return createSimpleRuleExpression(rule, this.effectiveTimeStart, this.effectiveTimeEnd);
                case JSON:
                    break;
                default:
            }
        }

        return null;
    }

    private boolean checkExpression(EncodeTypeEnum encodeType, SingleExpression singleExpression) {
        EncodeTypeEnum typeEnum = EncodeTypeEnum.parseFromType(singleExpression.getValueType());
        if (encodeType != typeEnum) {
            return false;
        }
        OperationType operationType = singleExpression.getOperationType();
        if (UNKNOWN == operationType) {
            return false;
        }
        try {
            encodeType.getTypeConvert().strDataConvert(singleExpression.getValue());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
