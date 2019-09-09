package com.xcodeassociated.cloud.user.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
public class UserRouter {
    @Bean
    RouterFunction<ServerResponse> routes(Environment env) {
        return RouterFunctions
            .route(GET("/router/message"),
                request -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
    }
}
