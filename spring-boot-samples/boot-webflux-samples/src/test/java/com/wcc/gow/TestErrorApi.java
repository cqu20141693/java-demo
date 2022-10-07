package com.wcc.gow;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * wcc 2022/9/27
 */
@Slf4j
public class TestErrorApi {

    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder().setNameFormat("rule-engine").build());
    private FluxSink<String> output;

    @Test
    public void testResume() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Disposable disposable = Flux.<String>create(sink -> this.output = sink).doOnNext(v -> {
                    if (v.equals("exception")) {
                        throw new RuntimeException(v);
                    } else if (v.equals("completion")) {
                        log.info("completion");
                        // output.complete();
                    } else if (v.equals("createErrorData")) {
                        log.info("onErrorResume");
                    }
                    log.warn("receive msg {}", v);
                })
//                .onErrorContinue((err, res) -> log.error(err.getMessage(), err)) // 可以恢复异常
                .onErrorResume(resume())
                .subscribe(System.out::println);

        executor.scheduleAtFixedRate(() -> simulationData(output), 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> check(disposable, countDownLatch), 3, 10, TimeUnit.SECONDS);
        countDownLatch.await();
    }

    private static Function<Throwable, Publisher<? extends String>> resume() {
        return err -> {
            log.info("fire error event");
            return Mono.just("createErrorData");
        };
    }

    private void check(Disposable disposable, CountDownLatch countDownLatch) {
        // 当流主动完成时，可以检查到disposable.isDisposed()
        if (disposable == null || disposable.isDisposed()) {
            log.info("disposed");
            countDownLatch.countDown();
        } else {
            log.info("common");
        }
    }

    private void simulationData(FluxSink<String> output) {
        String random = RandomStringUtils.random(8);
        int i = RandomUtils.nextInt(0, 100);

        if (i > 90) {
            output.next(random);
        } else if (i > 30) {
            output.next("completion");
        } else {
            output.next("exception");
        }

    }

}
