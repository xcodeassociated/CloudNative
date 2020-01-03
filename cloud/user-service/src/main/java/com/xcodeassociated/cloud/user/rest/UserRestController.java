package com.xcodeassociated.cloud.user.rest;

import com.xcodeassociated.cloud.user.dto.UserQueryRequestDto;
import com.xcodeassociated.cloud.user.dto.UserQueryResponseDto;
import com.xcodeassociated.cloud.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/v1/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    Mono<ResponseEntity<?>> getUserByNameAndPassword(@RequestBody UserQueryRequestDto request) {
        Optional<UserQueryResponseDto> userQueryDto = userService.getUserByData(request);
        if (userQueryDto.isPresent()) {
            return Mono.just(ResponseEntity.ok(userQueryDto.get()));
        } else {
            return Mono.just(ResponseEntity.notFound().build());
        }
    }

}
