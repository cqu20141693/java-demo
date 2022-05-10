package com.cc.flowable.strategy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * 系统调用
 * wcc 2022/5/10
 */
@Data
@Slf4j
public class CallExternalSystemDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Calling the external system for employee "
                + execution.getVariable("employee"));
    }
}
