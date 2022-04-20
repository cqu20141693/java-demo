package com.gow.expression.test;

import com.gow.expression.test.model.DataInfo;
import com.gow.expression.test.model.PropertyModel;
import com.gow.expression.test.model.SimpleContextModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@Component
@Slf4j
public class SpELTest {
    private static ExpressionParser parser = new SpelExpressionParser();

    @PostConstruct
    public void test() {
        useage();
    }

    public static void main(String[] args) {
        useage();
    }

    private static void useage() {
        //evaluate the literal string expression
        literalExpressionTest();

        // method invocation
        methodInvocationTest();
        //calling a JavaBean property calls the String property Bytes
        // invokes 'getBytes()'
        invokePropertyMethodTest();

        simpleContextTest();

        StandardEvaluationContext context = new StandardEvaluationContext();
        DataInfo dataInfo = new DataInfo().setStream("test-int").setType(1).setValue(2);
        DataInfo dataInfo1 = new DataInfo().setStream("test-int").setType(1).setValue(1);

        String condition = "type==1 && value>1";
        Expression expression = parser.parseExpression(condition);
        Boolean flag = expression.getValue(context, dataInfo, Boolean.class);
        if (flag != null && flag) {
            log.info("trigger action");
        }
        flag = expression.getValue(context, dataInfo1, Boolean.class);
        if (flag != null && flag) {
            log.info("trigger action");
        }
        // 首先数据模型要校验通过
        try {
            DataInfo dataInfo2 = new DataInfo().setStream("test-int").setType(1).setValue("1");
            flag = expression.getValue(context, dataInfo2, Boolean.class);
            if (flag != null && flag) {
                log.info("trigger simple action");
            }
        } catch (Exception e) {
            log.info("expression error,msg={}", e.getMessage());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("height", 2);
        String condition2 = "type==2";
        String fieldPath = "value.height";
        String condition3 = ">1";
        DataInfo dataInfo3 = new DataInfo().setStream("test-int").setType(2).setValue(map);

        Expression expression1 = parser.parseExpression(condition2);
        Boolean aBoolean = expression1.getValue(dataInfo3, Boolean.class);
        if (aBoolean != null && aBoolean) {

            String[] split = fieldPath.split("\\.");
            String root = split[0];
            Map value = parser.parseExpression(root).getValue(dataInfo3, Map.class);

            for (int i = 1; i < split.length; i++) {
                if (i == split.length - 1) {
                    Object data = value.get(split[i]);
                    condition3 = data.toString() + condition3;
                    Boolean value1 = parser.parseExpression(condition3).getValue(Boolean.class);
                    if (value1 != null && value1) {
                        log.info("trigger object action");
                    }
                } else {
                    value = (Map) value.get(split[i]);
                }
            }
        }


    }

    private static void simpleContextTest() {
        SimpleContextModel simple = new SimpleContextModel();
        simple.getBooleanList().add(true);
        //Custom PropertyAccessor only (no reflection)
        //Data binding properties for read-only access
        //Data binding properties for read and write
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        // "false" is passed in here as a String. SpEL and the conversion service
        // will recognize that it needs to be a Boolean and convert it accordingly.
        Expression parseExpression = parser.parseExpression("booleanList[0]");
        parseExpression.setValue(context, simple, "true");

        // b is false
        Boolean b = simple.getBooleanList().get(0);
        // - auto null reference initialization
        // - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);

        ExpressionParser parser = new SpelExpressionParser(config);

        Expression expression = parser.parseExpression("stringList[3]");
        // demo.list will now be a real collection of 4 entries
        // Each entry is a new empty String
        Object o = expression.getValue(simple);
        log.info("simple list bool={},string={}", b, o);
    }

    private static void invokePropertyMethodTest() {
        String s = "'Hello World'";
        Expression exp = parser.parseExpression(s + ".bytes");
        byte[] bytes = (byte[]) exp.getValue();
        exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        PropertyModel propertyModel = new PropertyModel().setName("gow").setType("developer");
        exp = parser.parseExpression("name");
        String name = (String) exp.getValue(propertyModel);
        exp = parser.parseExpression("name == 'gow'");
        boolean result = exp.getValue(propertyModel, Boolean.class);
        log.info("message bytes={},length={},name={},result={}", bytes, length, name, result);
    }

    private static void methodInvocationTest() {
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
    }

    private static void literalExpressionTest() {
        String helloWorld = "'Hello World'";
        Expression exp = parser.parseExpression(helloWorld);
        String message = (String) exp.getValue();

        String doubleString = "6.0221415E+23";
        Double aDouble = (Double) parser.parseExpression(doubleString).getValue();
        aDouble = Optional.ofNullable(aDouble).orElse(0.0);
        // evals to 2147483647

        Integer integer = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        integer = Optional.ofNullable(integer).orElse(0);
        Boolean aBoolean = (Boolean) parser.parseExpression("true").getValue();
        aBoolean = Optional.ofNullable(aBoolean).orElse(true);
        Object nullValue = parser.parseExpression("null").getValue();

        log.info("message={},double={},integer={},bool={},object={}", message, aDouble, integer, aBoolean, nullValue);
    }
}
