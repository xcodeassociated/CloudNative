package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.dto.EventDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import com.xcodeassociated.cloud.event.service.EventService;
import lombok.extern.log4j.Log4j2;
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
                        serverRequest -> ServerResponse.ok().body(this.eventService.getAllEvents(), EventDto.class))
                .andRoute(GET("/router/message"),
                        request -> ServerResponse.ok().body(Flux.just(Objects.requireNonNull(env.getProperty("message"))), String.class));
    }
}
