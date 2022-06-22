package com.cc.sip;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;

/**
 * 默认sip 消息处理器
 * wcc 2022/6/15
 */
@Slf4j
public class DefaultMessageListener implements SipListener {

    private final SipEventHandler sipEventHandler;

    public DefaultMessageListener(SipEventHandler sipEventHandler) {
        this.sipEventHandler = sipEventHandler;
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        sipEventHandler.handleEvent(requestEvent);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        sipEventHandler.handleEvent(responseEvent);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        log.info("occur timeout event={}", JSONObject.toJSONString(timeoutEvent));
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
        log.info("occur io exception event={}", JSONObject.toJSONString(ioExceptionEvent));
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        log.info("occur transactionTerminated event={}", JSONObject.toJSONString(transactionTerminatedEvent));
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        log.info("occur dialogTerminated event={}", JSONObject.toJSONString(dialogTerminatedEvent));
    }
}
