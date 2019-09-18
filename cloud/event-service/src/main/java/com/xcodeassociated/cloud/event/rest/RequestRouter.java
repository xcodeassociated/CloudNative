package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.rest.exception.EventCommandDtoParseException;
import com.xcodeassociated.cloud.event.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RequestRouter {
    private final EventService eventService;

    public RequestRouter(EventService eventService) {
        this.eventService = eventService;
    }

    @Bean
    RouterFunction<ServerResponse> routes(Environment env) {
        return RouterFunctions
            .route(GET("/router/events/{id}"),
                request -> {
                    String id = request.pathVariable("id");
                    return ok().body(this.eventService.getAllEvents(), EventQueryDto.class);
                }
            )
            .andRoute(POST("/router/create"),
                request ->
                    ok()
                        .body(request.body(toMono(EventCommandDto.class))
                                .onErrorResume(e ->
                                    Mono.error(new EventCommandDtoParseException(
                                        HttpStatus.BAD_REQUEST,
                                        "Request body parse exception", e)
                                    )
                                )
                            .flatMap(e -> this.eventService.createEvent(Mono.just(e)))
                            ,EventQueryDto.class)
                )
            .andRoute(DELETE("/router/delete/{id}/{by}"),
                request ->
                    ok()
                        .body(Mono.just(
                            new EventCommandDto(request.pathVariable("id"),
                                null, request.pathVariable("by"))
                        ).flatMap(e -> this.eventService.removeEvent(Mono.just(e))), Void.class)
                )
            // diagnostic api
            .andRoute(GET("/router/message"),
                request ->
                    ok().body(Mono.just(Objects.requireNonNull(env.getProperty("message"))), String.class));
    }
}
