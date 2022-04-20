package com.gow;

import java.util.Optional;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wujt  2021/6/4
 */
public class MonoTest {
    public static void main(String[] args) {

        // Create a new Mono that emits the specified item, which is captured at instantiation time.
        Mono.just("are").subscribe(System.out::println);
        //Create a Mono that completes without emitting any item.
        Mono.empty().subscribe(System.out::println);
        // producing its value using the provided Supplier. If the Supplier resolves to null, the resulting Mono
        // completes empty.
        Mono.fromSupplier(() -> "you").subscribe(System.out::println);
        //Create a new Mono that emits the specified item if Optional.isPresent() otherwise only emits onComplete.
        Mono.justOrEmpty(Optional.of("ok")).subscribe(System.out::println);
        Mono.justOrEmpty(Optional.empty()).subscribe(System.out::println);

        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);


        Mono.just("world").concatWith(Mono.create((sink) -> {
            sink.success(" 你好");
        })).subscribe(System.out::println);

        Mono.just("hello").flatMap(s -> {
            return Mono.just(s.length());
        }).subscribe(System.out::println);

        // Mono 中返回多个Mono,不自动包装
        Function<Boolean, Mono<? extends Boolean>> booleanMonoFunction = flag -> Mono.just(true);
        Mono<String> result = Mono.just(false).flatMap(v -> {
            if (v) {
                return Mono.just("success");
            } else {
                return Mono.just("failed");
            }
        });
        Mono<Boolean> success = result.map(v -> v.equals("success"));
        success.block();

        Mono<Void> monoVoid = Flux.just("go", "java", "cc").any(e -> {
            System.out.println(e);
            return false;
        }).flatMap(r -> {
            System.out.println(r);

            return Mono.empty();
        });
        monoVoid.subscribe(System.out::println);


        //  mono empty hanlder
        System.out.println("test mono empty start");
        Mono<Boolean> booleanMono = getResult(false);
        Mono<String> mono = booleanMono.hasElement().flatMap(v -> {
            if(v) {
                System.out.println("has element");
               return booleanMono.flatMap(value->{
                   if (value) {
                       return Mono.just("success");
                   } else {
                       return Mono.just("failed");
                   }
               });
            }else {
                System.out.println("not element");
            }
            return Mono.just("failed");
        });
        mono.subscribe(System.out::println);
        System.out.println("test mono empty end");
    }

    private static Mono<Boolean> getResult(Boolean flag) {
        if (flag) {
            return Mono.just(true);
        }
        return Mono.empty();
    }

}
