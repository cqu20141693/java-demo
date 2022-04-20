package com.gow.expression.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@Data
@Accessors(chain = true)
public class PropertyModel {
    private String name;
    private String type;
}
