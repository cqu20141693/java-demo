package com.gow.strategy.trigger;

import com.alibaba.fastjson.JSONObject;
import com.gow.codec.model.EncodeTypeEnum;
import com.gow.strategy.trigger.domain.ConjunctionType;
import com.gow.strategy.trigger.domain.MultiExpression;
import com.gow.strategy.trigger.domain.OperationType;
import com.gow.strategy.trigger.domain.SimpleTypeRule;
import com.gow.strategy.trigger.domain.SingleExpression;
import com.gow.strategy.trigger.domain.TriggerRuleModel;
import com.gow.strategy.trigger.domain.TriggerRuleType;
import com.gow.strategy.trigger.domain.TriggerStrategy;
import com.gow.strategy.trigger.model.StreamTriggerRuleModel;
import com.gow.strategy.trigger.model.TriggerStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author gow
 * @date 2021/9/2
 */
public class SimpleRuleTriggerTest {

    @Test
    @DisplayName("createRule")
    public void createRule() {

        EncodeTypeEnum encodeType = EncodeTypeEnum.TYPE_INT;

        TriggerRuleModel triggerRuleModel = new TriggerRuleModel();
        triggerRuleModel.setType(TriggerRuleType.SIMPLE);
        triggerRuleModel.setEffectiveTimeEnd(0);
        triggerRuleModel.setEffectiveTimeEnd(1440);

        SimpleTypeRule simpleRule = new SimpleTypeRule();
        simpleRule.setIndex("#data");

        TreeSet<MultiExpression> multiExpressions = new TreeSet<>();
        simpleRule.setExpressions(new ArrayList<>(multiExpressions));
        MultiExpression multiExpression = new MultiExpression();
        multiExpressions.add(multiExpression);
        multiExpression.setType(ConjunctionType.AND);
        HashSet<SingleExpression> singleExpressions = new HashSet<>();
        multiExpression.setSimpleRules(singleExpressions);
        singleExpressions.add(createExpression(encodeType, OperationType.EQ, "1"));

        triggerRuleModel.setRule((JSONObject) JSONObject.toJSON(simpleRule));

        TriggerStrategy triggerStrategy = new TriggerStrategy();
        HashSet<String> strategyKeys = new HashSet<>();
        strategyKeys.add("strategy1");
        triggerStrategy.setStrategyKeys(strategyKeys);

        StreamTriggerRuleModel model = new StreamTriggerRuleModel().setRueId(1)
                .setRuleName("test-int")
                .setStatus(TriggerStatus.ACTIVE)
                .setBizKey("sass")
                .setGroupKey("groupKey")
                .setSn("sn1")
                .setStream("int-channel")
                .setTriggerRule(triggerRuleModel)
                .setContext(triggerStrategy);

        //(#data == 1) and #time >= 0 and #time < 1440
        TriggerRuleModel triggerRule = model.getTriggerRule();
        Expression expression = triggerRule.generate(encodeType);
        testSingleExpression(expression);

        // (#data < 10 and #data > 0) and #time >= 0 and #time < 1440
        HashSet<SingleExpression> expressions = new HashSet<>();
        multiExpression.setSimpleRules(expressions);
        expressions.add(createExpression(encodeType, OperationType.GT, "0"));
        expressions.add(createExpression(encodeType, OperationType.LT, "10"));

        triggerRuleModel.setRule((JSONObject) JSONObject.toJSON(simpleRule));
        expression = triggerRule.generate(encodeType);
        testSingleExpression(expression);


        //
        MultiExpression multiExpression1 = new MultiExpression();
        multiExpression1.setType(ConjunctionType.OR);
        multiExpression1.setSimpleRules(expressions);
        multiExpressions.add(multiExpression1);
        multiExpression1.setOrder(1);

        triggerRuleModel.setRule((JSONObject) JSONObject.toJSON(simpleRule));
        expression = triggerRule.generate(encodeType);
        testSingleExpression(expression);

    }

    private SingleExpression createExpression(EncodeTypeEnum encodeType, OperationType operationType, String value) {
        SingleExpression singleExpression = new SingleExpression();
        singleExpression.setOperationType(operationType);
        singleExpression.setValueType(encodeType.getTypeName());
        singleExpression.setValue(value);
        return singleExpression;
    }

    private void testSingleExpression(Expression expression) {
        //触发处理数据
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("data", 1);

        //设置消息分钟相对值
        context.setVariable("time", 1);
        Boolean value = expression.getValue(context, Boolean.class);
        System.out.println(expression.getExpressionString() + " : " + value);
        context.setVariable("data", 2);
        value = expression.getValue(context, Boolean.class);

        System.out.println(expression.getExpressionString() + " : " + value);
    }

    public static void main(String[] args) {

        SpelExpressionParser parser = new SpelExpressionParser();

        String expression = "(#data >0) or (#age >0)";
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("data", -1);
        context.setVariable("age", -1);

        Expression parseExpression = parser.parseExpression(expression);
        Boolean value = parseExpression.getValue(context, Boolean.class);
        System.out.println(expression + " : " + value);

        expression = "(#data <0)";
        parseExpression = parser.parseExpression(expression);
        value = parseExpression.getValue(context, Boolean.class);
        System.out.println(expression + " : " + value);
    }


    @Test
    @DisplayName("test TreeSet")
    public void testTreeSet() {
        MultiExpression multiExpression = new MultiExpression();
        multiExpression.setOrder(0);
        multiExpression.setType(ConjunctionType.AND);
        MultiExpression multiExpression1 = new MultiExpression();
        multiExpression1.setOrder(1);
        multiExpression1.setType(ConjunctionType.OR);
        MultiExpression multiExpression2 = new MultiExpression();
        multiExpression2.setOrder(-1);
        multiExpression2.setType(ConjunctionType.UNKNOWN);


        TreeSet<MultiExpression> objects = new TreeSet<>();
        objects.add(multiExpression);
        objects.add(multiExpression1);
        objects.add(multiExpression2);
        for (MultiExpression expression : objects) {
            System.out.println(expression.getOrder() + ":" + expression.getType());
        }
    }
}
