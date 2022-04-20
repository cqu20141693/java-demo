package com.gow;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author gow 2021/06/03
 */
public class FluxTest {
    public static void main(String[] args) throws InterruptedException {
        // 通过给定值创建Flux ,
        //A Reactive Streams Publisher with rx operators that emits 0 to N elements,
        Flux<String> justFlux = Flux.just("Hello", "World");
        //Subscribe a Consumer to this Flux that will consume all the elements in the sequence
        justFlux.subscribe(System.out::println);

        Flux.from(Flux.empty()).take(1).subscribe();

        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 4).subscribe(System.out::println);
        //Create a Flux that emits long values starting with 0 and incrementing at specified time intervals on the
        // global timer.
        Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).take(2).subscribe(System.out::println);

        //Programmatically create a Flux by generating signals one-by-one via a consumer callback.
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        //Programmatically create a Flux by generating signals one-by-one via a consumer
        // callback and some state. The stateSupplier may return null.
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                // 退出 sink
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
        // FluxSink
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
        // filter Evaluate each source value against the given Predicate. If the predicate test succeeds, the value
        // is emitted.
        // If the predicate test fails, the value is ignored and a request of 1 is made upstream.
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        //take only the first N values from this Flux, if available.
        //If N is zero, the resulting Flux completes as soon as this Flux signals its first value (which is not not
        // relayed, though)
        Flux.range(1, 20).take(10).subscribe(System.out::println);
        //Emit the last N values this Flux emitted before its completion.
        Flux.range(1, 20).takeLast(10).subscribe(System.out::println);
        Flux.range(1, 5).take(10).subscribe(System.out::println);

        //Relay values from this Flux while a predicate returns TRUE for the values
        Flux.range(1, 20).takeWhile(i -> i < 10).subscribe(System.out::println);
        //lay values from this Flux until the given Predicate matches
        Flux.range(1, 20).takeUntil(i -> i == 10).subscribe(System.out::println);
        //Reduce the values from this Flux sequence into a single object of the same type than the emitted items
        Flux.range(1, 10).reduce((x, y) -> x + y).subscribe(System.out::println);
        //Reduce the values from this Flux sequence into a single object matching the type of a lazily supplied seed
        // value
        Flux.range(1, 10).reduceWith(() -> 10, (x, y) -> x + y).subscribe(System.out::println);
        //Merge data from Publisher sequences contained in an array / vararg into an interleaved merged sequence.
        Flux<Long> merge = Flux.merge(
                Flux.interval(Duration.of(500, ChronoUnit.MILLIS)).take(2),
                Flux.interval(Duration.of(500, ChronoUnit.MILLIS)).take(2),
                Flux.create(longFluxSink -> {
                    Flux.range(0, 10).toStream().forEach(index -> longFluxSink.next(Long.valueOf(index)));
                    longFluxSink.complete();
                })
        );
        merge.toStream().forEach(System.out::println);

        //Collect incoming values into multiple List buffers that will be emitted by the returned Flux
        // each time the given max size is reached or once this Flux completes
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        //Collect incoming values into multiple List buffers that will be emitted by the resulting Flux
        // each time the given predicate returns true.
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        // Collect incoming values into multiple List buffers that will be emitted by the resulting Flux.
        // Each buffer continues aggregating values while the given predicate returns true, and a new buffer
        // is created as soon as the predicate returns false
        Flux.range(1, 10).bufferWhile(i -> i % 3 < 2).subscribe(System.out::println);

        //<T2> Flux<reactor.util.function.Tuple2<T, T2>>
        //Zip this Flux with another Publisher source, that is to say wait for both to emit one element and combine
        // these elements once into a Tuple2. The operator will continue doing so until any of the sources completes
        Flux<Tuple2<String, String>> tuple2Flux = Flux.just("a", "b", "c", "d")
                .zipWith(Flux.just("e", "f", "g", "h", "i"));
        tuple2Flux.map((s1) -> String.format("%s-%s", s1.getT1(), s1.getT2())).subscribe(System.out::println);
        tuple2Flux
                .subscribe(System.out::println);
        //Zip this Flux with another Publisher source, that is to say wait for both to emit one element and combine
        // these elements using a combinator BiFunction The operator will continue doing so until any of the sources
        // completes.
        Flux.just("a", "b", "c", "d")
                .zipWith(Flux.just("e", "f", "g", "h", "i"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);

        //Mono: A Reactive Streams Publisher with basic rx operators that emits at most one item via
        // the onNext signal then terminates with an onComplete signal (successful Mono

        //Concatenate emissions of this Flux with the provided Publisher (no interleave).
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
        Flux.just(1, 2)
                .concatWith(Flux.empty())
                .subscribe(System.out::println, System.err::println);
        //Simply emit a captured fallback value when any error is observed on this Flux.
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);

        // Subscribe to a returned fallback publisher when any error occurs, using a function to choose the fallback
        // depending on the error.
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                }).subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Flux.create(integerFluxSink -> {
                    integerFluxSink.next(1);
                    throw new IllegalArgumentException();
                }))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                }).subscribe(System.out::println);
        Thread.currentThread().join(2000);


        testListToFlux();

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        singleOrEmpty(list);
        list.remove(0);
        singleOrEmpty(list);
        list.add(2);
        singleOrEmpty(list);
    }

    private static void singleOrEmpty(ArrayList<Integer> list) {
        Flux.fromIterable(list)
                .flatMap(num -> {
                    if ((num & 2) == 0) {
                        return Mono.just(num);
                    } else {
                        return Mono.empty();
                    }
                })
                .take(1)
                .singleOrEmpty()
                .switchIfEmpty(Mono.from(Mono.just(1)))
                .subscribe(System.out::println);

    }

    private static void testListToFlux() {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("success");
        strings.add("failed");
        strings.add("123");
        Flux<String> stringFlux = Flux.fromStream(strings.stream());
        Mono<Boolean> failed = stringFlux.any(s ->
                s.equals("failed"));
        if (failed.toFuture().join()) {
            System.out.println("failed");
        }


        Flux<String> fromIterable = Flux.fromIterable(strings);
        Flux<String> map = fromIterable.flatMap((s) ->
                Mono.just(s.toUpperCase()))
                .map(v -> v);
        String s = map.blockFirst();
        map.subscribe(System.out::println);


    }
}
