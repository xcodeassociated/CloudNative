package com.locator.rest;

import com.locator.model.Message;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author ardiansyah
 */
@RestController
public class ResourceREST {

	WebClient client = WebClient.create("http://localhost:8000");

	ResourceREST() {
		System.out.println(">>> ResourceREST");
	}

	@RequestMapping(value = "/resource/home", method = RequestMethod.GET)
	public Mono<String> home(){
		return Mono.just("home page");
	}

	@RequestMapping(value = "/resource/message", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<String> message(){
		return client.get()
				.uri("/message")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class);
	}

	@RequestMapping(value = "/resource/reservations", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Flux<String> reservations(){
		return client.get()
				.uri("/routerreservations")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(String.class);
	}

	@RequestMapping(value = "/resource/user", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public Mono<ResponseEntity<?>> user() {

		return Mono.just(ResponseEntity.ok(new Message("Content for user")));
	}
	
	@RequestMapping(value = "/resource/admin", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ResponseEntity<?>> admin() {
		return Mono.just(ResponseEntity.ok(new Message("Content for admin")));
	}
	
	@RequestMapping(value = "/resource/user-or-admin", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Mono<ResponseEntity<?>> userOrAdmin() {
		return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
	}
}
