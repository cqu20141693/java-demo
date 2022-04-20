package com.gow.spel;

import com.alibaba.fastjson.JSONObject;
import com.gow.strategy.trigger.domain.OperationType;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author gow
 * @date 2021/9/2 0002
 */
public class SpelTest {
    SpelExpressionParser parser = new SpelExpressionParser();

    @Test
    @DisplayName("SPEL String test ")
    public void testString() {
        //触发处理数据
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("data", "cc");
        String ltExpression = "#data " + OperationType.LT.getCode() + " 'bc'";
        Expression lt = parser.parseExpression(ltExpression);
        Boolean value = lt.getValue(context, Boolean.class);
        System.out.println(value);
        context.setVariable("data", "aa");
        value = lt.getValue(context, Boolean.class);
        System.out.println(value);

        String eqExpression = "#data " + OperationType.EQ.getCode() + " 'bc'";
        Expression eq = parser.parseExpression(eqExpression);
        context.setVariable("data", "bc");
        value = eq.getValue(context, Boolean.class);
        System.out.println(value);

        String neExpression = "#data " + OperationType.NE.getCode() + " 'bc'";
        Expression ne = parser.parseExpression(neExpression);
        context.setVariable("data", "bb");
        value = ne.getValue(context, Boolean.class);
        System.out.println(value);
    }

    @Test
    @DisplayName("SPEL object test")
    public void testObject() throws JSONException {
        HashMap<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("name", "gow");
        jsonObject.put("age", 20);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("height", 165);
        jsonObject.put("info", hashMap);
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject object1 = new JSONObject();
        object1.put("employee", "zc");
        jsonObjects.add(object1);
        jsonObject.put("employees", jsonObjects);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(jsonObject);
        String express = "get('name') " + OperationType.EQ.getCode() + " 'gow'";
        Expression expression = parser.parseExpression(express);
        Boolean value = expression.getValue(context, Boolean.class);
        System.out.println(express + ":" + value);

        express = "get('age') " + OperationType.LT.getCode() + " 23";
        Expression ageExpression = parser.parseExpression(express);
        value = ageExpression.getValue(context, Boolean.class);
        System.out.println(express + ":" + value);

        String nestedExpression = "get('info').get('height') " + OperationType.LT.getCode() + " 23";
        Expression infoExpression = parser.parseExpression(nestedExpression);
        value = infoExpression.getValue(context, Boolean.class);
        System.out.println(nestedExpression + ":" + value);

        String arrayExpression = "get('employees')[0].get('employee')";
        Expression arrayExpressions = parser.parseExpression(arrayExpression);
        String employee = arrayExpressions.getValue(context, String.class);
        System.out.println(arrayExpression + ":" + value);

        arrayExpression = "get('employees')[0].get('employee') " + OperationType.NE.getCode() + " 'lc'";
        arrayExpressions = parser.parseExpression(arrayExpression);
        value = arrayExpressions.getValue(context, Boolean.class);
        System.out.println(arrayExpression + ":" + value);

        // JSONObject test
        String jsonString = JSONObject.toJSONString(jsonObject);
        JSONObject object = JSONObject.parseObject(jsonString);
        context.setRootObject(object);
        nestedExpression = "get('info').get('height') " + OperationType.LT.getCode() + " 170";
        infoExpression = parser.parseExpression(nestedExpression);
        value = infoExpression.getValue(context, Boolean.class);
        System.out.println(nestedExpression + ":" + value);

    }
}
