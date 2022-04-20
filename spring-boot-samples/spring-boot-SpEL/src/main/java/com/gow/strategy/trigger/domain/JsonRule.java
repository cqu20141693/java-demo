package com.gow.strategy.trigger.domain;

import java.util.TreeSet;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/2
 */
@Data
public class JsonRule {

    private ConjunctionType type;
    private TreeSet<SimpleTypeRule> subRules;

}
