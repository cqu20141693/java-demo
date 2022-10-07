package com.wcc;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * wcc 2022/9/30
 */
@Data
public class FluxDemo {

    @Test
    public void test(){
        Flux.just(1,2,3,4,5)
                .window(3,2)
                .flatMap(f->f.reduce(0, (x1, x2) -> x1 + x2))
                .subscribe(System.out::println);
    }
}
