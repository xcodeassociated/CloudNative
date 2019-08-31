package com.xcodeassociated.cloud.gateway.rest.reactive;

import com.xcodeassociated.cloud.gateway.rest.reactive.error.DefaultErrorCode;
import com.xcodeassociated.cloud.gateway.rest.reactive.error.ErrorCodeInterface;
import com.xcodeassociated.cloud.gateway.rest.reactive.exception.NameRequiredException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class ReactiveRequestHandler {

    @Bean
    public RouterFunction<ServerResponse> routeRequest() {
        return RouterFunctions.route(
                RequestPredicates.GET("/reactive/message")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), this::handleMessageRequest)
                .andRoute(
                        RequestPredicates.GET("/reactive/whoami")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), this::handleWhoAmIRequest)
                .andRoute(
                        RequestPredicates.GET("/reactive/error")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), this::handleRequestWithException);

    }

    public Mono<ServerResponse> handleRequestWithException(ServerRequest request) {
        return ServerResponse.ok()
                .body(exceptionDuringProcess(request)
                        .onErrorResume(e ->
                                Mono.error(new NameRequiredException(
                                        HttpStatus.BAD_REQUEST, "please provide a name", e))), String.class);
    }

    private Mono<String> exceptionDuringProcess(ServerRequest request) {
        try {
            throw new Exception("error");
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private Mono<String> handleMessage(ServerRequest request) {
        return Mono.just(
                new JSONObject()
                        .put("forward", request.queryParam("message").get())
                        .toString());
    }

    private Mono<ServerResponse> handleMessageRequest(ServerRequest request) {
        return handleMessage(request)
                .onErrorReturn("error return")
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .syncBody(s));
    }

    private Mono<JSONObject> data(Principal principal, SecurityContext securityContext) {
        return Mono.just(new JSONObject()
                .put("principal", principal.getName())
                .put("authentication", securityContext.getAuthentication().getName()));
    }

    private <T extends ErrorCodeInterface> JSONObject fallBack(T t) {
        return new JSONObject()
                .put(t.getErrorMessage(), t.getCode());
    }

    private Mono<ServerResponse> handleWhoAmIRequest(ServerRequest request) {
        return Mono.zip(
                request.principal(),
                ReactiveSecurityContextHolder.getContext()
        ).flatMap(tuple -> {
            var principal = tuple.getT1();
            var securityContext = tuple.getT2();
            return data(principal, securityContext)
                    .onErrorReturn(fallBack(
                            new DefaultErrorCode
                                    .DefaultErrorCodeBuilder("handleWhoAmIRequest", 1)
                                    .build())
                    ).flatMap(response ->
                            ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .syncBody(response.toString()));
        });
    }
}