package com.locator;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@EnableEurekaClient
@SpringBootApplication
public class ReservationClientApplication {

//	@Bean
//	RouteLocator gateway(RouteLocatorBuilder routeLocatorBuilder) {
//		return routeLocatorBuilder
//				.routes()
//				.route(
//					specification -> specification
//						.host("*.gw.sc")
//							.and()
//						.path("/api/reservations")
//							.filters(
//									filter -> filter
//											.setPath("/reservations")
//							)
//							.uri("lb://reservation-com.locator.service/")
//				)
//				.build();
//	}


	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

//	@Bean
//	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//
//		http
//			.authorizeExchange()
//			.pathMatchers("/login", "/")
//				.authenticated()
//			.and()
//			.addFilterAt(basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
//			.authorizeExchange()
//			.pathMatchers("/api/**")
//				.hasRole("USER") //2
//// 					.authenticated() //1
//			.and()
//			.addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
//
//		return http.build();
//	}

}
