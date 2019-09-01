package com.xcodeassociated.cloud.gateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@Log4j2
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
	    log.debug("Application Context Running");
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}




