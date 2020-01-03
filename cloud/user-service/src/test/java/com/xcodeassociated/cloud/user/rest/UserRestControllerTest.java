package com.xcodeassociated.cloud.user.rest;

import com.xcodeassociated.cloud.user.dto.UserQueryRequestDto;
import com.xcodeassociated.cloud.user.dto.UserQueryResponseDto;
import com.xcodeassociated.cloud.user.model.UserRole;
import com.xcodeassociated.cloud.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@WebFluxTest(UserRestController.class)
public class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private WebTestClient client;

    @Before
    public void setUp() {
        this.client = WebTestClient
            .bindToController(userRestController)
            .build();

        Mockito.lenient().when(this.userService.getUserByData(any(UserQueryRequestDto.class)))
            .thenReturn(Optional.of(new UserQueryResponseDto(null, "user", "password", Arrays.asList(UserRole.ROLE_USER)))).getMock();
    }

    @Test
    public void httpUserQueryResponseDto_CorrectRequest_Test() {
        this.client.post()
            .uri("/v1/user/get")
            .body(BodyInserters
                .fromObject(new UserQueryRequestDto("user", "password")))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.username").isNotEmpty()
            .jsonPath("$.username").isEqualTo("user")
            .jsonPath("$.password").isNotEmpty()
            .jsonPath("$.password").isEqualTo("password")
            .jsonPath("$.roles").isEqualTo("ROLE_USER");
    }

    @Test
    public void httpUserQueryResponseDto_IncorrectRequest_Test() {
        this.client.post()
            .uri("/v1/user/get")
            .body(BodyInserters
                .fromObject("incorrect_request_type"))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .is4xxClientError();
    }
}
