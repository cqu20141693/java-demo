package com.cc.sip;

import lombok.extern.slf4j.Slf4j;

import javax.sip.*;

/**
 * sip 处理消息lister
 * wcc 2022/5/14
 */
@Slf4j
public class LogSipListener implements SipListener {

    public static boolean logEnabled() {
        return true;
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        log.debug("SIP request:\n{}", requestEvent.getRequest());
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        log.debug("SIP response:\n{}", responseEvent.getResponse());
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        Transaction transaction = timeoutEvent.getServerTransaction() == null
                ? timeoutEvent.getClientTransaction()
                : timeoutEvent.getServerTransaction();
        log.warn("SIP timeout:\n{}", transaction.getRequest());
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
        log.warn("SIP IO Exception: {}://{}:{}",
                ioExceptionEvent.getTransport(),
                ioExceptionEvent.getHost(),
                ioExceptionEvent.getPort());
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent event) {
        Transaction transaction = event.getServerTransaction() == null
                ? event.getClientTransaction()
                : event.getServerTransaction();
        log.trace("SIP TransactionTerminated:\n{}", transaction.getRequest());
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent event) {
        log.trace("SIP DialogTerminated:\n{}", event.getDialog().getDialogId());
    }
}
