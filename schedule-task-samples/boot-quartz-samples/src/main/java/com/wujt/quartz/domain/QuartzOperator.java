package com.wujt.quartz.domain;

import org.quartz.spi.OperableTrigger;

import java.util.List;

/**
 * @author wujt
 */
public interface QuartzOperator {
     Boolean insertTrigger();
     Boolean updateTrigger();
     Boolean deleteTrigger();
     List<OperableTrigger> selectTrigger(String  jobKey);
     Boolean selectTrigger();



}
