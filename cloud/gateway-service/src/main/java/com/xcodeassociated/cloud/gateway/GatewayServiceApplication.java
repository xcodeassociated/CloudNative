package com.xcodeassociated.cloud.gateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@Log4j2
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class GatewayServiceApplication {
	public static void main(String[] args) {
	    log.debug("Application Context Running");
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
}




