package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
public class RequestRouter {
    private final EventService eventService;

    public RequestRouter(EventService eventService) {
        this.eventService = eventService;
    }

    @Bean
    RouterFunction<ServerResponse> routes(Environment env) {
        return RouterFunctions
            .route(GET("/router/events"),
                serverRequest ->
                    ServerResponse.ok().body(this.eventService.getAllEvents(),
                        EventQueryDto.class))

            // diagnostic api
            .andRoute(GET("/router/message"),
                request ->
                    ServerResponse.ok()
                        .body(Flux.just(Objects.requireNonNull(env.getProperty("message"))),
                            String.class));
    }
}
