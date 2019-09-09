package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
public class RequestRouter {
    @Bean
    RouterFunction<ServerResponse> routes(EventRepository eventRepository, Environment env) {
        return RouterFunctions
                .route(GET("/router/events"),
                        serverRequest -> ServerResponse.ok().body(eventRepository.findAll(), Event.class))
                .andRoute(GET("/router/message"),
                        request -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
    }
}
