package io.spring.start.javanative.reservationclient;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@Component
class GlobalErrorAttributes extends DefaultErrorAttributes {
	private HttpStatus status = HttpStatus.BAD_REQUEST;
	private String message = "please provide a name";

	public GlobalErrorAttributes() {
		super(false);
	}

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
		map.put("status", getStatus());
		map.put("message", getMessage());
		return map;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}

@Component
@Order(-2)
class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
	public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
										  ResourceProperties resourceProperties,
										  ApplicationContext applicationContext,
										  ServerCodecConfigurer configure) {
		super(errorAttributes, resourceProperties, applicationContext);
		super.setMessageWriters(configure.getWriters());
		super.setMessageReaders(configure.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
		return ServerResponse.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters
						.fromObject(errorPropertiesMap));
	}
}

class NameRequiredException extends ResponseStatusException {
	public NameRequiredException(HttpStatus status, String message, Throwable e) {
		super(status, message, e);
	}
}

@Log4j2
@EnableEurekaClient
@SpringBootApplication
@RestController
public class ReservationClientApplication {
	public static void main(String[] args) {
	    log.debug("Application Context Running");
		SpringApplication.run(ReservationClientApplication.class, args);
	}
}

@Service
class RestReactiveRouter {

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
							.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), this::handleRequest4);

	}

	public Mono<ServerResponse> handleRequest4(ServerRequest request) {
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



