package com.gow.pulsar.producer;

import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.springframework.scheduling.TaskScheduler;

/**
 * @author gow
 * @date 2021/7/9
 */
@Slf4j
public class CommonProducer<V> implements PulsarProducer<V> {

    private final Producer<V> commonProducer;

    private final TaskScheduler taskScheduler;

    public CommonProducer(Producer<V> commonProducer) {
        this.commonProducer = commonProducer;
        taskScheduler = null;
    }

    public CommonProducer(Producer<V> commonProducer, TaskScheduler taskScheduler) {
        this.commonProducer = commonProducer;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public ScheduledFuture<?> scheduleSendAsync(long initialDelay, long fixDelay, Supplier<String> keySupplier,
                                                Supplier<V> valueSupplier) {
        assert taskScheduler != null : "PulsarProducer not support schedule send,must be config taskScheduler";
        Date date = new Date(System.currentTimeMillis() + initialDelay);
        return taskScheduler.scheduleWithFixedDelay(() -> sendAsync(keySupplier, valueSupplier), date, fixDelay);
    }

    @Override
    public ScheduledFuture<?> scheduleSend(long initialDelay, long fixDelay, Supplier<String> keySupplier,
                                           Supplier<V> valueSupplier) {
        assert taskScheduler != null : "PulsarProducer not support schedule send,must be config taskScheduler";
        Date date = new Date(System.currentTimeMillis() + initialDelay);
        return taskScheduler.scheduleWithFixedDelay(() -> send(keySupplier, valueSupplier), date, fixDelay);
    }

    @Override
    public CompletableFuture<Boolean> sendAsync(Supplier<String> keySupplier,
                                                Supplier<V> valueSupplier) {
        CompletableFuture<MessageId> future = commonProducer.newMessage()
                .eventTime(System.currentTimeMillis())
                .properties(new HashMap<>())
                .key(keySupplier.get())
                .value(valueSupplier.get()).sendAsync();
        return handle(keySupplier, valueSupplier, future);
    }

    @Override
    public CompletableFuture<Boolean> sendDelayAsync(Supplier<String> keySupplier, Supplier<V> valueSupplier,
                                                     long delay, TimeUnit timeUnit) {
        CompletableFuture<MessageId> future = commonProducer.newMessage()
                .deliverAfter(delay, timeUnit)
                .eventTime(System.currentTimeMillis())
                .properties(new HashMap<>())
                .key(keySupplier.get())
                .value(valueSupplier.get()).sendAsync();
        return handle(keySupplier, valueSupplier, future);
    }

    private CompletableFuture<Boolean> handle(Supplier<String> keySupplier,
                                              Supplier<V> valueSupplier,
                                              CompletableFuture<MessageId> future) {
        return future.handle((msgId, ex) -> {
            if (ex == null) {
                log.info("Message send async success persisted2: key={},value={},msgId={}", keySupplier.get(),
                        valueSupplier.get(), msgId.toString());
                return true;
            } else {
                log.error("message send async failed:key={},value={} ", keySupplier.get(), valueSupplier.get(), ex);
                return false;
            }
        });
    }

    @Override
    public boolean send(Supplier<String> keySupplier, Supplier<V> valueSupplier) {
        try {
            if(!commonProducer.isConnected()){

            }
            MessageId messageId = commonProducer.newMessage()
                    .key(keySupplier.get())
                    .value(valueSupplier.get())
                    .eventTime(System.currentTimeMillis())
                    .send();
            log.info("send success key={},value={},msgId={}", keySupplier.get(), valueSupplier.get(),
                    JSONObject.toJSON(messageId));
            return true;
        } catch (Exception e) {
            log.info("send failed key={},value={}", keySupplier.get(), valueSupplier.get());
        }
        return false;
    }

    @Override
    public boolean sendDelay(Supplier<String> keySupplier, Supplier<V> valueSupplier, long delay,
                             TimeUnit timeUnit) {
        try {
            MessageId messageId = commonProducer.newMessage()
                    .key(keySupplier.get())
                    .value(valueSupplier.get())
                    .deliverAfter(delay, timeUnit)
                    .eventTime(System.currentTimeMillis())
                    .send();
            log.info("send success key={},value={},msgId={}", keySupplier.get(), valueSupplier.get(),
                    JSONObject.toJSON(messageId));
            return true;
        } catch (Exception e) {
            log.info("send failed key={},value={}", keySupplier.get(), valueSupplier.get());
        }
        return false;
    }
}

