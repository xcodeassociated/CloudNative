package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@Component
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
            })
            .andRoute(POST("/router/create"),
                request -> {
                    Mono<EventCommandDto> eventCommandDto = request.body(toMono(EventCommandDto.class));
                    Mono<EventQueryDto> eventAdded = this.eventService.createEvent(eventCommandDto);
                    return ok().body(eventAdded, EventQueryDto.class);
                })
            .andRoute(DELETE("/router/delete/{id}/{by}"),
                request -> {
                    String id = request.pathVariable("id");
                    String by = request.pathVariable("by");
                    Mono<EventCommandDto> eventCommandDto = Mono.just(new EventCommandDto(id, null, by));
                    Mono<Void> result = this.eventService.removeEvent(eventCommandDto);
                    return ok().body(result, Void.class);
                })
            // diagnostic api
            .andRoute(GET("/router/message"),
                request ->
                    ok().body(Mono.just(Objects.requireNonNull(env.getProperty("message"))), String.class));
    }
}
