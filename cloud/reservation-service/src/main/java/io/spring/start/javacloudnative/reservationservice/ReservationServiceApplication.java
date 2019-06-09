package io.spring.start.javacloudnative.reservationservice;

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

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Log4j2
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	@Bean
	RouterFunction<ServerResponse> routes(ReservationRepository reservationRepository, Environment env) {
		return RouterFunctions
				.route(GET("/routerreservations"),
					serverRequest -> ServerResponse.ok().body(reservationRepository.findAll(), Reservation.class))
				.andRoute(GET("/message"),
								request -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
	}

	public static void main(String[] args) {
		log.debug("Application Context Running");
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}

@Component
class QueueReceiver implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		var queueName = "hello_queue";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}

}

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
class Reservation {
	private String id;
	private String reservationName;
}

interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {}

@Component
class DataWriter implements ApplicationRunner {

	private final ReservationRepository reservationRepository;

	public DataWriter(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.reservationRepository
				.deleteAll()
				.thenMany(
					Flux.just("Alice", "Bob", "Mike", "Steve")
							.map(name -> new Reservation(null, name))
							.flatMap(this.reservationRepository::save))
				.thenMany(this.reservationRepository.findAll())
					.subscribe(System.out::println);
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

@Component
class Receiver {

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
