package com.locator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
				.exceptionHandling()
				.authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() ->
						swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
				).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() ->
						swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
				).and()
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				.authorizeExchange()
				.pathMatchers(HttpMethod.OPTIONS).permitAll()

				.pathMatchers("/login").permitAll()
				.pathMatchers("/resource/home").permitAll()
				.pathMatchers("/resource/rabbit").permitAll()
				.pathMatchers("/resource/noauth-reservations").permitAll()

				.anyExchange().authenticated()
				.and()
				.build();
	}
}
