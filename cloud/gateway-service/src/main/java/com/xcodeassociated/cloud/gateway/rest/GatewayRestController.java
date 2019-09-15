package com.xcodeassociated.cloud.gateway.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.loadbalancer.Server;
import com.xcodeassociated.cloud.gateway.dto.Message;
import com.xcodeassociated.cloud.gateway.security.model.UserSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
@RestController
@Api(value = "Gateway REST endpoints documentation")
public class GatewayRestController {

	@Qualifier("loadBalancedWebClientBuilder")
	@Autowired
	private WebClient.Builder client;

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

	@ApiOperation(value = "Home Page")
	@RequestMapping(value = "/pub/home", method = RequestMethod.GET)
	public Mono<?> home(){
		return Mono.just("todo: home");
	}

	@ExceptionHandler()
	public Mono<ResponseEntity<?>> handleException(Exception e) {
	    log.debug("Handling Exception: {}", e.getMessage());
	    return Mono.just(ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(e.getLocalizedMessage()));
    }

	@ApiOperation(value = "Returns JSON Array of Events")
    @RequestMapping(value = "/resource/events", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Flux<?> getEvents(Authentication authentication) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserSubject userSubject = objectMapper.readValue(authentication.getName(), UserSubject.class);
		return HystrixCommands
            .from(client
                .baseUrl("http://event-service")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/router/events/{id}")
                    .build(userSubject.getId()))
                .retrieve()
                .bodyToFlux(String.class))
            .fallback(Flux.just(new JSONArray().toString()))
            .commandName("getEvents")
            .toFlux();
	}

    @ApiOperation(value = "Returns created event")
    @RequestMapping(value = "/resource/event/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> addEvent(@RequestBody Map<String, String> data, Authentication authentication) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserSubject userSubject = objectMapper.readValue(authentication.getName(), UserSubject.class);
        data.put("id", userSubject.getId().toString());
        log.debug(">>> {}", data.toString());
        return HystrixCommands
            .from(client
                .baseUrl("http://event-service")
                .build()
                .post()
                .uri("/router/create")
                .body(BodyInserters
                    .fromObject(data))
                .exchange()
                .flatMap(response -> response.toEntity(String.class)))
            .fallback(Mono.just(ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Could not reach event service")))
            .commandName("addEvent")
            .toMono();
    }

    @ApiOperation(value = "Deletes event")
    @RequestMapping(value = "/resource/event/delete", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteEvent(@RequestParam("id") String id, Authentication authentication) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserSubject userSubject = objectMapper.readValue(authentication.getName(), UserSubject.class);
        return HystrixCommands
            .from(client
                .baseUrl("http://event-service")
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                    .path("/router/delete/{id}/{by}")
                    .build(id, userSubject.getId()))
                .exchange()
                .flatMap(response -> response.toEntity(String.class)))
            .fallback(Mono.just(ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Could not reach event service")))
            .commandName("deleteEvent")
            .toMono();
    }

	// security and diagnostics api

    @ApiOperation(value = "Returns Event Service diagnostics message")
    @RequestMapping(value = "/resource/event/message", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<String> eventServiceMessage(){
        return HystrixCommands
            .from(client.build().get()
                .uri("http://event-service/router/message")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class))
            .fallback(Mono.just(new JSONObject().toString()))
            .commandName("getEventMessage")
            .toMono();
    }

    @ApiOperation(value = "Returns Event Service diagnostics message")
    @RequestMapping(value = "/resource/user/message", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<String> userServiceMessage(){
        return HystrixCommands
            .from(client.build().get()
                .uri("http://user-service/router/message")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class))
            .fallback(Mono.just(new JSONObject().toString()))
            .commandName("getUserMessage")
            .toMono();
    }


    @ApiOperation(value = "Returns User Role - diagnostics")
	@ApiResponses(
			value = {
					@ApiResponse(code = 401, message = "Unauthorized"),
					@ApiResponse(code = 200, message = "Success operation: Returns authentication auth data")
			}
	)
	@RequestMapping(value = "/resource/diag/whoami", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<String> whoAmI(Authentication authentication, Principal principal) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserSubject authSubject = objectMapper.readValue(authentication.getName(), UserSubject.class);
        UserSubject principalSubject = objectMapper.readValue(principal.getName(), UserSubject.class);
        return Mono.just(new JSONObject()
				.put("authentication", authSubject)
				.put("principal", principalSubject)
				.toString()
		);
	}

}
