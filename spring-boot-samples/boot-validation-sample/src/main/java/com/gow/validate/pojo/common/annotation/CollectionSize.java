package com.gow.validate.pojo.common.annotation;

import com.gow.validate.pojo.common.CollectionLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wujt  2021/5/18
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {CollectionLengthValidator.class})
public @interface CollectionSize {

    int fixLength() default 0;

    int maxLength() default 0;

    int minLength() default 0;

    validStrategy strategy() default validStrategy.max;

    // 默认错误消息
    String message() default "collection size error";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

    static enum validStrategy {
        /**
         * 集合固定长度
         *
         * @date 2021/5/18 23:11
         * @param null
         * @return null
         */
        fix {
            @Override
            public boolean valid(CollectionSize collectionSize, Collection collection) {
                if (collection == null && collectionSize.fixLength() == 0) {
                    return true;
                } else {
                    return collectionSize.fixLength() == collection.size();
                }
            }
        },
        max {
            @Override
            public boolean valid(CollectionSize collectionSize, Collection collection) {
                if (collection == null || collection.size() == 0) {
                    return true;
                } else {
                    return collectionSize.maxLength() == collection.size();
                }
            }
        },
        min {
            @Override
            public boolean valid(CollectionSize collectionSize, Collection collection) {
                if (collection == null && collectionSize.minLength() == 0) {
                    return true;
                } else {
                    return collectionSize.minLength() == collection.size();
                }
            }
        },
        range {
            @Override
            public boolean valid(CollectionSize collectionSize, Collection collection) {
                boolean isEmpty = collectionSize.minLength() == 0 && collectionSize.maxLength() == 0;
                if (collection == null && collectionSize.minLength() == 0) {
                    return true;
                } else {
                    return collectionSize.minLength() <= collection.size() && collectionSize.maxLength() >= collection.size();
                }
            }
        };

        public abstract boolean valid(CollectionSize collectionSize, Collection collection);
    }
}
