package com.xcodeassociated.cloud.event;

import com.xcodeassociated.cloud.event.config.ConfigStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

@Log4j2
@EnableDiscoveryClient
@SpringBootApplication
public class EventServiceApplication {

	public static void main(String[] args) {
		log.debug("Application Context Running");
		SpringApplication.run(EventServiceApplication.class, args);
	}

}
