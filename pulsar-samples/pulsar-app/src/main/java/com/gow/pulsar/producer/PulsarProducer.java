package com.gow.pulsar.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author gow
 * @date 2021/7/9
 */
public interface PulsarProducer<V> {

    CompletableFuture<Boolean> sendAsync(Supplier<String> keySupplier,
                                         Supplier<V> valueSupplier);

    CompletableFuture<Boolean> sendDelayAsync(Supplier<String> keySupplier,
                                              Supplier<V> valueSupplier, long delay, TimeUnit timeUnit);

    boolean send(Supplier<String> keySupplier, Supplier<V> valueSupplier);

    boolean sendDelay(Supplier<String> keySupplier, Supplier<V> valueSupplier, long delay, TimeUnit timeUnit);

    ScheduledFuture<?> scheduleSendAsync(long initialDelay, long fixDelay, Supplier<String> keySupplier,
                                         Supplier<V> valueSupplier);

    ScheduledFuture<?> scheduleSend(long initialDelay, long fixDelay, Supplier<String> keySupplier, Supplier<V> valueSupplier);
}
