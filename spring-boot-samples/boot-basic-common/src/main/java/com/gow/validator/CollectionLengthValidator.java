package com.gow.validator;


import com.gow.validator.annotation.CollectionSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author wujt  2021/5/18
 */
public class CollectionLengthValidator implements ConstraintValidator<CollectionSize, Collection> {
    private CollectionSize collectionSize;

    @Override
    public void initialize(CollectionSize constraintAnnotation) {
        check(constraintAnnotation);
        collectionSize = constraintAnnotation;
    }

    private void check(CollectionSize constraintAnnotation) {
        assert constraintAnnotation.fixLength() >= 0 : "fixLength must be >= 0";
        assert constraintAnnotation.minLength() >= 0 : "minLength must be >= 0";
        assert constraintAnnotation.maxLength() >= constraintAnnotation.minLength() : "maxLength must be >=minLength";
    }

    @Override
    public boolean isValid(Collection collection, ConstraintValidatorContext constraintValidatorContext) {
        return collectionSize.strategy().valid(collectionSize, collection);
    }
}
