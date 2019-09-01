package com.xcodeassociated.cloud.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
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
				// swagger ui
				.pathMatchers("/api/**", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
				// public access endpoints
				.pathMatchers("/resource/login").permitAll()
				.pathMatchers("/pub-api/**").permitAll()
				// webflux router public
				.pathMatchers("/reactive/**").permitAll()
				// webflux restricted access
				.pathMatchers("/resource/**").access((mono, context) -> mono
						.map(auth -> auth.getAuthorities().stream().anyMatch(e ->
								e.getAuthority().equals("ROLE_ADMIN") || e.getAuthority().equals("ROLE_USER")))
						.map(AuthorizationDecision::new))
				// ---
				.anyExchange()
				.authenticated()
				.and()
				.build();
	}
}
