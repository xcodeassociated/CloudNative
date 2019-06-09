package com.locator.rest;

import com.locator.model.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Log4j2
@RestController
public class ResourceREST {
	protected WebClient client = WebClient.create("http://localhost:8000");
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private static final String QUEUE_NAME = "hello_queue";

	ResourceREST() throws TimeoutException, IOException {
        this.factory = new ConnectionFactory();
        factory.setHost("localhost");
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	@RequestMapping(value = "/resource/rabbit", method = RequestMethod.GET)
	public Mono<String> rabbit() throws TimeoutException, IOException {
		String message = "ping";
		if (channel.isOpen()) {
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			log.info(" [x] Sent '" + message + "'");
			return Mono.just("Message has been sent...");
		} else {
			log.error("Channel is closed!");
			return Mono.just("Message has NOT sent...");
		}
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

	@RequestMapping(value = "/resource/noauth-reservations", method = RequestMethod.GET)
	public Flux<String> noAuthReservations(){
		return client.get()
				.uri("/routerreservations")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(String.class);
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
