package com.gow.strategy.trigger.model;

/**
 * @author gow
 * @date 2021/9/2
 */
public enum TriggerStatus {
    NOT_ACTIVE,
    // 三方事务，脏数据
    INIT,
    ACTIVE;
}
