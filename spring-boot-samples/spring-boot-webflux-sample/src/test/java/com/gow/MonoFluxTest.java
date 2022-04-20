package com.gow;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gow 2021/06/04
 */
@Slf4j
public class MonoFluxTest {
    public static void main(String[] args) {
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("接受到数据: " + item);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("处理完了!");
            }

        };

        String[] strs = {"1", "2", "3"};

        // map -> Flux<String>->flux<Integer>
        Flux<Integer> flux = Flux.fromArray(strs)
                .map(Integer::parseInt);
        System.out.println(flux.blockLast());
        flux.subscribe(subscriber);
        Mono.fromSupplier(() -> 1).map(s -> s + 1).subscribe(subscriber);
        System.out.println("test flatMap");
        // flatMap : flux<Integer>->Flux<Publisher>
        Flux.just("you", "are", "successful").flatMap(str -> Mono.just(str.length())).subscribe(System.out::println);
        System.out.println("test switchIfEmpty ");
        // switchIfEmpty :  如果为空，切换提供publisher
        Flux.from(Flux.range(0, 8))
                .flatMap(num -> {
                    if (num > 4) {
                        return Mono.empty();
                    } else {
                        return Flux.range(0, num);
                    }
                })
//                .take(10)
                .take(0)
                .switchIfEmpty(Flux.range(0, 10))
                .subscribe(System.out::println);
        // defaultIfEmpty： 如果为空默认值
        Mono.empty().defaultIfEmpty(Mono.just("default")).subscribe(System.out::println);

        // flatMapMany : Mono<String>->Flux<String>
        System.out.println("test flatMapMany");
        Mono.just("you are successful").flatMapMany(str -> Flux.fromArray(str.split(" "))).subscribe(System.out::println);

        // test error

        Flux.from(Flux.error(new Exception("test error")))
                .onErrorResume(err -> {
                    log.error(err.getMessage(), err);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.just("resume"))
                .subscribe(System.out::println);

        // then : 当前结束，开启下一个流程
        System.out.println("test then");
        Mono.just("success").then(Mono.just("failed")).subscribe(System.out::println);

        // error
        System.out.println("test error handler");
        // onErrorResume 流以error结束，进行错误恢复处理
        Mono.error(new Exception("error")).onErrorResume(err -> {
            return Mono.just("resume");
        }).subscribe(System.out::println);
        // doOnError： 接受error流，消费错误信息，继续以error事件传递
        Mono.error(new Exception("error")).doOnError(err -> {
            log.info("{},over", err.getMessage());
        }).onErrorResume(err -> {
            return Mono.just("resume");
        }).subscribe(System.out::println);

        System.out.println("test defer lazy publisher 1");
        // defer publish 发生在just as之后，因为subscribe方法最后订阅了它
        AtomicInteger integer = new AtomicInteger(0);
        Flux.just("success").concatMap(msg -> {
            System.out.println("just publisher:"+integer.getAndIncrement());
            return Flux.defer(() -> {
                System.out.println("defer publisher:"+integer.getAndIncrement());
                return Flux.just("print:"+integer.getAndIncrement());
            });
        })
                .as(func -> {
                    System.out.println("just as:"+integer.getAndIncrement());
                    return func.cast(String.class);
                }).subscribe(System.out::println);
        System.out.println("test defer lazy publisher 2");
        Flux.just("success").concatMap(msg -> {
            System.out.println("just publisher:"+integer.getAndIncrement());
            return Flux.defer(() -> {
                System.out.println("defer publisher:" + integer.getAndIncrement());
                return Flux.just("print:" + integer.getAndIncrement());
            });
        })
                .as(func -> {
                    func.subscribe();
                    System.out.println("just as:"+integer.getAndIncrement());
                   // func.cast(String.class);
                    return Flux.just("just");
                }).subscribe(System.out::println);

        System.out.println("test defer lazy publisher 3");
        Flux.just("just").map(just->{
            System.out.println("map");
            return Flux.just(just);
        }).as(just->{
            just.subscribe();
            System.out.println("as");
            return Flux.defer(()->Flux.just("defer"));
        }).subscribe(System.out::println);

        System.out.println("test defer lazy publisher 4");
        Flux.just("just").distinct(just->{
            System.out.println("map");
            return Flux.just(just);
        }).as(just->{
            just.subscribe();
            System.out.println("as");
            return Flux.defer(()->Flux.just("defer"));
        }).subscribe(System.out::println);
    }

}
