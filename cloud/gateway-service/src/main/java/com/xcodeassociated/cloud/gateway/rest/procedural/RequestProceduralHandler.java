package com.xcodeassociated.cloud.gateway.rest.procedural;

import com.xcodeassociated.cloud.gateway.event.EventHandler;
import com.xcodeassociated.cloud.gateway.model.Message;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.TimeoutException;


@Log4j2
@RestController
public class RequestProceduralHandler {
	private WebClient client = WebClient.create("http://localhost:8000");
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private static final String QUEUE_NAME = "hello_queue";

	RequestProceduralHandler() throws TimeoutException, IOException {
        this.factory = new ConnectionFactory();
        factory.setHost("localhost");
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
		this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	// public

	// rabbitMQ
	@RequestMapping(value = "/pub-api/rabbit", method = RequestMethod.GET, params = {"message"})
	public Mono<String> rabbit(@RequestParam(value = "message") String data) throws IOException {
		if (this.channel.isOpen()) {
			this.channel.basicPublish("", QUEUE_NAME, null, data.getBytes("UTF-8"));
			log.info(" [x] Sent '" + data + "'");
			return Mono.just("Message: {" + data + "} has been sent.");
		} else {
			log.error("Channel is closed!");
			return Mono.just("Message: {" + data + "} has NOT been send! ");
		}
	}

	@RequestMapping(value = "/pub-api/rabbit-rpc", method = RequestMethod.GET)
	public Mono<String> rabbitRPC(@RequestParam("message") String data) {
		try (EventHandler client = new EventHandler()) {
			log.info(" [x] Requesting: {" + data + "}");
			String response = client.call(data);
			log.info(" [.] Got '" + response + "'");
			return Mono.just("{" + response + "}");
		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
			return Mono.just("Error occurred");
		}
	}
	// ! rabbitMQ


	@RequestMapping(value = "/pub-api/path/{message}", method = RequestMethod.GET)
	public Mono<String> pathVar(@PathVariable String message) throws IOException {
		return Mono.just("{" + message + "}");
	}

	@RequestMapping(value = "/pub-api/home", method = RequestMethod.GET)
	public Mono<String> home(){
		return Mono.just("home page");
	}

	// todo: refactor - wrap the hystrix call
	@RequestMapping(value = "/pub-api/message", method = RequestMethod.GET)
	public Mono<String> pubMessage(){
		return HystrixCommands
				.from(client.get()
						.uri("/router/message")
						.accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.bodyToMono(String.class))
				.fallback(Mono.just("Fallback"))
				.commandName("getMessage")
				.toMono();
	}

	// todo: refactor - wrap the hystrix call
	@RequestMapping(value = "/pub-api/reservations", method = RequestMethod.GET)
	public Flux<String> pubReservations(){
		return HystrixCommands
				.from(client.get()
						.uri("/router/reservations")
						.accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.bodyToFlux(String.class))
				.fallback(Mono.just("Fallback"))
				.commandName("getReservations")
				.toFlux();
	}

	// restricted

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
		return client.get()
				.uri("/router/message")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class);
	}

	@RequestMapping(value = "/resource/reservations", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Flux<String> reservations(){
		return client.get()
				.uri("/router/reservations")
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
