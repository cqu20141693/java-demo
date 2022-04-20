package com.gow.expression.test.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
@Data
public class SimpleContextModel {
    private List<Boolean> booleanList = new ArrayList<Boolean>();
    private List<String> stringList;
}
