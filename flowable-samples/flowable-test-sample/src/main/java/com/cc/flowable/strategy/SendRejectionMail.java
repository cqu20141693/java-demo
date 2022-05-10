package com.cc.flowable.strategy;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * 发送拒绝邮件
 * wcc 2022/5/10
 */
@Slf4j
public class SendRejectionMail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("send reject mail for employee {}", execution.getVariable("employee"));
    }
}
