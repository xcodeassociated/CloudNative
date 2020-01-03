package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {RequestRouter.class})
@WebFluxTest()
public class RequestRouterTest {

    @Mock
    private EventService eventService;

    @Mock
    private Environment env;

    @InjectMocks
    private RequestRouter router;

    @Before
    public void setUp() {
        Flux<EventQueryDto> testEventDtos = Flux.just(
            new EventQueryDto(1L, "Event 1")
        );

        Mockito.lenient().when(this.eventService.getAllEvents())
            .thenReturn(testEventDtos);

        Mockito.lenient().when(this.eventService.createEvent(any()))
            .thenReturn(Mono.just(new EventQueryDto(2L, "Event 2")));

        Mockito.lenient().when(this.eventService.removeEvent(any()))
            .thenReturn(Mono.empty());
    }

    @Test
    public void httpGetEventsWithResponseDto_CorrectRequest_Test() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(router.routes(env))
            .build();

        client.get()
            .uri("/v1/router/events/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
                .jsonPath("$.[0].eventId").isNotEmpty()
                .jsonPath("$.[0].eventId").isEqualTo("1")
                .jsonPath("$.[0].eventName").isNotEmpty()
                .jsonPath("$.[0].eventName").isEqualTo("Event 1");
    }

    @Test
    public void httpEventCreateWithResponseDto_CorrectRequest_Test() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(router.routes(env))
            .build();

        client.post()
            .uri("/v1/router/create")
            .body(BodyInserters
                .fromObject(new EventCommandDto(2L, "Event 2", 1L)))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
                .jsonPath("$.eventId").isNotEmpty()
                .jsonPath("$.eventId").isEqualTo("2")
                .jsonPath("$.eventName").isNotEmpty()
                .jsonPath("$.eventName").isEqualTo("Event 2");
    }

    @Test
    public void httpEventCreateWithResponseDto_IncorrectRequest_Test() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(router.routes(env))
            .build();

        client.post()
            .uri("/v1/router/create")
            .body(BodyInserters
                .fromObject("incorrect_request_type"))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .is4xxClientError();
    }

    @Test
    public void httpDeleteEventRequest_CorrectRequest_Test() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(router.routes(env))
            .build();

        client.delete()
            .uri("/v1/router/delete/1/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk();
    }
}
