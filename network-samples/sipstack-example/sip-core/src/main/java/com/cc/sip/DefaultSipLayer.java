package com.cc.sip;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * 默认sip layer
 * 统一listeners 处理入口
 * wcc 2022/5/14
 */
@Data
@Slf4j
public class DefaultSipLayer implements SipLayer, SipListener {
    private final SipStack sipStack;
    private final SipProvider tcpProvider;
    private final SipProvider udpProvider;
    private final MessageFactory messageFactory;
    private final HeaderFactory headerFactory;
    private final AddressFactory addressFactory;

    private final List<SipListener> listeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    @SneakyThrows
    DefaultSipLayer init() {
        tcpProvider.addSipListener(this);
        udpProvider.addSipListener(this);
        if (LogSipListener.logEnabled()) {
            addListener(new LogSipListener());
        }
        return this;
    }

    @Override
    public void dispose() {
        sipStack.stop();
        listeners.clear();
        disposed.set(true);
    }

    @Override
    public boolean isDisposed() {
        return disposed.get();
    }

    @Override
    public void addListener(SipListener listener) {
        listeners.add(listener);
    }

    private <T> void doListener(T event, BiConsumer<SipListener, T> consumer) {
        for (SipListener sipListener : listeners) {
            try {
                consumer.accept(sipListener, event);
            } catch (Throwable e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        doListener(requestEvent, SipListener::processRequest);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        doListener(responseEvent, SipListener::processResponse);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        doListener(timeoutEvent, SipListener::processTimeout);
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
        doListener(ioExceptionEvent, SipListener::processIOException);
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        doListener(transactionTerminatedEvent, SipListener::processTransactionTerminated);
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        doListener(dialogTerminatedEvent, SipListener::processDialogTerminated);
    }
}
