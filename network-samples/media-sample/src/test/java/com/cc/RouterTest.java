package com.cc;

import com.cc.topic.Router;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;


public class RouterTest {


    @Test
    public void test() {

        Router<String, String> router = Router.<String, String>create()
                .route("/device/*/on", Mono::just)
                .route("/device/*/off", v -> Mono.just(v.toLowerCase()));

        router.execute("/device/test/on", "on")
                .flatMap(Function.identity())
                .as(StepVerifier::create)
                .expectNext("on")
                .verifyComplete();

        router.execute("/device/test/off", "OFF")
                .flatMap(Function.identity())
                .as(StepVerifier::create)
                .expectNext("off")
                .verifyComplete();


    }

}
