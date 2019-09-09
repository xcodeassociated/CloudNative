package com.xcodeassociated.cloud.gateway.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.xcodeassociated.cloud.gateway.security.dto.UserQueryResponseServiceDto;
import com.xcodeassociated.cloud.gateway.security.dto.UserQueryResponseTokenDto;
import com.xcodeassociated.cloud.gateway.security.model.UserSubject;
import com.xcodeassociated.cloud.gateway.security.utils.JWTUtil;
import com.xcodeassociated.cloud.gateway.security.utils.PBKDF2Encoder;
import com.xcodeassociated.cloud.gateway.security.dto.UserQueryRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Log4j2
@RestController
public class AuthenticationRestController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Qualifier("loadBalancedRestTemplate")
    @Autowired
    RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @HystrixCommand(fallbackMethod = "loginFallback")
    @RequestMapping(value = "/resource/login", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> login(@RequestBody UserQueryRequestDto userQueryRequestDto) throws JsonProcessingException {
        String password = passwordEncoder.encode(userQueryRequestDto.getPassword());
        String user = userQueryRequestDto.getUsername();

        HttpEntity<UserQueryRequestDto> request = new HttpEntity<>(new UserQueryRequestDto(user, password));
        ResponseEntity<UserQueryResponseServiceDto> response = this.restTemplate
            .postForEntity("http://user-service/user/get", request, UserQueryResponseServiceDto.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            UserQueryResponseServiceDto ret = response.getBody();
            if (ret != null) {
                UserSubject userSubject = new UserSubject(ret.getId(), ret.getUsername());
                log.debug(ret);
                log.debug(userSubject);

                return Mono.just(ResponseEntity.ok(new UserQueryResponseTokenDto(jwtUtil.generateToken(ret, userSubject))));
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            }
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public Mono<ResponseEntity<?>> exceptionUserNotFoundExceptionHandler(HttpClientErrorException exception) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ResponseEntity<?>> loginFallback(UserQueryRequestDto userQueryRequestDto) {
        return Mono.just(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build());
    }

}
