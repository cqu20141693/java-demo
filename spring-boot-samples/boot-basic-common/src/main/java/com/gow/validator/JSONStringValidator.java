package com.gow.validator;

import com.alibaba.fastjson.JSONObject;
import com.gow.validator.annotation.JSONString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author wujt  2021/5/18
 */
@Slf4j
public class JSONStringValidator implements ConstraintValidator<JSONString, String> {

    private Class clazz;

    @Override
    public void initialize(JSONString constraintAnnotation) {
        log.info("annotation={}", constraintAnnotation);
        clazz = constraintAnnotation.jsonClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || !value.isEmpty()) {
            return true;
        } else {
            try {
                JSONObject.parseObject(value, clazz);
                return true;
            } catch (Exception e) {
                log.error("json valid parse error,", e);
                return false;
            }

        }
    }
}
