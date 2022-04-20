package com.gow.strategy.trigger.model;

import com.gow.strategy.trigger.domain.TriggerRuleModel;
import com.gow.strategy.trigger.domain.TriggerStrategy;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/9/2
 */
@Data
@Accessors(chain = true)
public class StreamTriggerRuleModel {
    private int rueId;

    private String ruleName;

    private TriggerStatus status;

    private String bizKey;

    private String groupKey;

    private String sn;

    private String stream;

    private TriggerRuleModel triggerRule;

    private TriggerStrategy context;

}
