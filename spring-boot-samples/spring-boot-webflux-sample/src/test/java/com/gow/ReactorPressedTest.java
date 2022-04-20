package com.gow;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ReactorPressedTest {

    @Test

    public void testPressed() throws InterruptedException {
        //  log();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> log(countDownLatch)).start();
        countDownLatch.await();
    }

    private void log(CountDownLatch countDownLatch) {
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            integers.add(i);
        }
        Flux.fromIterable(integers)
                .log()
                .flatMap(index -> {
                    try {
                        Thread.sleep(100);
                        System.out.println("deploy");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Mono.just(index + "flatMap");
                })
                .log()
                .flatMap(v -> {
                    try {
                        Thread.sleep(100);
                        System.out.println("mock dataa");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Mono.just("success");
                })
                .log().subscribe();
        countDownLatch.countDown();
    }
}
