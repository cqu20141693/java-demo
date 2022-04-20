package com.gow.expression.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
@Data
@Accessors(chain = true)
public class DataInfo {
    private String stream;
    private Integer type;
    private Object value;
}
