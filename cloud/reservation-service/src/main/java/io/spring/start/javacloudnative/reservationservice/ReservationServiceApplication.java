package io.spring.start.javacloudnative.reservationservice;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	@Bean
	RouterFunction<ServerResponse> routes(ReservationRepository reservationRepository, Environment env) {
		return RouterFunctions
				.route(GET("/router/reservations"),
					serverRequest -> ServerResponse.ok().body(reservationRepository.findAll(), Reservation.class))
				.andRoute(GET("/router/message"),
								request -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
	}

	public static void main(String[] args) {
		log.debug("Application Context Running");
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
class Reservation  {
	private String id;
	private String reservationName;
}

class Greeting extends ResourceSupport {

	private final String content;

	@JsonCreator
	public Greeting(@JsonProperty("content") String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}

interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {}

@Log4j2
@Component
class DataWriter implements ApplicationRunner {

	private final ReservationRepository reservationRepository;

	public DataWriter(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		log.info("DataWriter Bean Context Started");
		this.reservationRepository
				.deleteAll()
				.thenMany(
					Flux.just("Alice", "Bob", "Mike", "Steve")
							.map(name -> new Reservation(null, name))
							.flatMap(this.reservationRepository::save))
				.thenMany(this.reservationRepository.findAll())
					.subscribe(log::debug);
	}
}

@RestController
class ReservationRestController {

	@Autowired
	private ReservationRepository reservationRepository;

	@GetMapping("/reservations")
	Flux<Reservation> getAll() {
		return this.reservationRepository.findAll();
	}
}

@RestController
class GreetingController {

	private static final String TEMPLATE = "Hello, %s!";

	@RequestMapping("/greeting")
	public HttpEntity<Greeting> greeting(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {

		Greeting greeting = new Greeting(String.format(TEMPLATE, name));
		greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());

		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}
}

// rabbitmq

@Log4j2
@Component
class QueueReceiver implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("QueueReceiver Bean Context Started");
		var queueName = "hello_queue";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, false, false, false, null);
		log.info(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			log.info(" [x] Received '" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}

}


@Log4j2
@Component
class RPCServer implements ApplicationRunner {
	private static final String RPC_QUEUE_NAME = "rpc_queue";

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("RPCServer Bean Context Started");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection();
			 Channel channel = connection.createChannel()) {
			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
			channel.queuePurge(RPC_QUEUE_NAME);

			channel.basicQos(1);

			log.info(" [x] Awaiting RPC requests");

			Object monitor = new Object();
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties
						.Builder()
						.correlationId(delivery.getProperties().getCorrelationId())
						.build();

				String response = "";
				try {
					String message = new String(delivery.getBody(), "UTF-8");

					log.info(" [.] got(" + message + ")");
					response += message + " processed";
				} catch (RuntimeException e) {
					log.error(" [.] " + e.toString());
				} finally {
					channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					// RabbitMq consumer worker thread notifies the RPC server owner thread
					synchronized (monitor) {
						monitor.notify();
					}
				}
			};

			channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
			// Wait and be prepared to consume the message from RPC client.
			while (true) {
				synchronized (monitor) {
					try {
						monitor.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

