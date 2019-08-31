package com.xcodeassociated.cloud.gateway.rest;

import com.xcodeassociated.cloud.gateway.model.Message;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
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
import com.rabbitmq.client.AMQP;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class RPCClient implements AutoCloseable {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";

	public RPCClient() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	public String call(String message) throws IOException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();

		String replyQueueName = channel.queueDeclare().getQueue();
		AMQP.BasicProperties props = new AMQP.BasicProperties
				.Builder()
				.correlationId(corrId)
				.replyTo(replyQueueName)
				.build();

		channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

		final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

		String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response.offer(new String(delivery.getBody(), "UTF-8"));
			}
		}, consumerTag -> { });

		String result = response.take();
		channel.basicCancel(ctag);
		return result;
	}

	public void close() throws IOException {
		connection.close();
	}
}

@Log4j2
@RestController
public class ResourceREST {
	private WebClient client = WebClient.create("http://localhost:8000");
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private static final String QUEUE_NAME = "hello_queue";

	ResourceREST() throws TimeoutException, IOException {
        this.factory = new ConnectionFactory();
        factory.setHost("localhost");
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
		this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	// public

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
		try (RPCClient client = new RPCClient()) {
			log.info(" [x] Requesting: {" + data + "}");
			String response = client.call(data);
			log.info(" [.] Got '" + response + "'");
			return Mono.just("{" + response + "}");
		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
			return Mono.just("Error occurred");
		}
	}

	@RequestMapping(value = "/pub-api/path/{message}", method = RequestMethod.GET)
	public Mono<String> pathVar(@PathVariable String message) throws IOException {
		return Mono.just("{" + message + "}");
	}

	@RequestMapping(value = "/pub-api/home", method = RequestMethod.GET)
	public Mono<String> home(){
		return Mono.just("home page");
	}

	@RequestMapping(value = "/pub-api/message", method = RequestMethod.GET)
	public Mono<String> pubMessage(){
		return client.get()
				.uri("/router/message")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class);
	}

	@RequestMapping(value = "/pub-api/reservations", method = RequestMethod.GET)
	public Flux<String> pubReservations(){
		return client.get()
				.uri("/router/reservations")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(String.class);
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
