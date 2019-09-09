package com.xcodeassociated.cloud.gateway.rest;

import com.xcodeassociated.cloud.gateway.dto.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;

@Log4j2
@RestController
@Api(value = "Resource REST Endpoint", description = "...")
public class GatewayRestController {

	@Qualifier("loadBalancedWebClientBuilder")
	@Autowired
	private WebClient.Builder client;

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

	// public

	@RequestMapping(value = "/pub-api/path/{message}", method = RequestMethod.GET)
	public Mono<String> pathVar(@PathVariable String message) {
		return Mono.just("{" + message + "}");
	}

	@ApiOperation(value = "Home Page")
	@RequestMapping(value = "/pub-api/home", method = RequestMethod.GET)
	public Mono<String> home(){
		return Mono.just("home page");
	}

	// todo: refactor - wrap the hystrix call
	@ApiOperation(value = "Returns ...")
	@RequestMapping(value = "/pub-api/message", method = RequestMethod.GET)
	public Mono<String> pubMessage(){
		return HystrixCommands
				.from(client.build().get()
						.uri("http://event-service/router/message")
						.accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.bodyToMono(String.class))
				.fallback(Mono.just("{}"))
				.commandName("getMessage")
				.toMono();
	}

	// todo: refactor - wrap the hystrix call
	@ApiOperation(value = "Returns ...")
	@RequestMapping(value = "/pub-api/reservations", method = RequestMethod.GET)
	public Flux<String> pubReservations(){
		return HystrixCommands
				.from(client.build().get()
						.uri("http://event-service/router/reservations")
						.accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.bodyToFlux(String.class))
				.fallback(Mono.just(new JSONArray(new ArrayList<String>()).toString()))
				.commandName("getReservations")
				.toFlux();
	}

	// restricted
	@ApiOperation(value = "Returns User Role")
	@ApiResponses(
			value = {
					@ApiResponse(code = 100, message = "..."),
					@ApiResponse(code = 200, message = "Success operation")
			}
	)
	@RequestMapping(value = "/resource/whoami", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<String> whoAmI(Authentication authentication, Principal principal) {
		return Mono.just(new JSONObject()
				.put("authentication", authentication.getName())
				.put("principal", principal.getName())
				.toString()
		);
	}

	@RequestMapping(value = "/resource/message", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<String> message(){
		return client.build().get()
				.uri("http://event-service/router/message")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class);
	}

	@RequestMapping(value = "/resource/reservations", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Flux<String> reservations(){
		return client.build().get()
				.uri("http://event-service/router/reservations")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(String.class);
	}

	@RequestMapping(value = "/resource/user", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public Mono<ResponseEntity<?>> user() {
		return Mono.just(ResponseEntity.ok(new Message("Content for user")));
	}

	@RequestMapping(value = "/resource/user-or-admin", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<ResponseEntity<?>> userOrAdmin() {
		return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
	}

	@RequestMapping(value = "/resource/admin", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ResponseEntity<?>> admin() {
		return Mono.just(ResponseEntity.ok(new Message("Content for admin")));
	}

}
