package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.model.Reservation;
import com.xcodeassociated.cloud.event.service.ReservationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Log4j2
@Component
public class RequestRouter {

    @Bean
    RouterFunction<ServerResponse> routes(ReservationRepository reservationRepository, Environment env) {
        return RouterFunctions
                .route(GET("/router/reservations"),
                        serverRequest -> ServerResponse.ok().body(reservationRepository.findAll(), Reservation.class))
                .andRoute(GET("/router/message"),
                        request -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
    }

}
