package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Flux<EventQueryDto> testEventDtos;

    @Before
    public void setUp() {
        Flux<Event> testEvents = Flux.just(
            new Event("1", "Event 1"),
            new Event("2", "Event 2")
        );

        this.testEventDtos = Flux.just(
            new EventQueryDto("1", "Event 1"),
            new EventQueryDto("2", "Event 2")
        );

        Mockito.lenient().when(this.eventRepository.findAll())
            .thenReturn(testEvents)
            .thenReturn(testEvents.take(1));

        Mockito.lenient().when(this.eventRepository.deleteById(anyString()))
            .thenReturn(Mono.empty());

        Mockito.lenient().when(this.eventRepository.save(any(Event.class)))
            .thenReturn(Mono.just(new Event("3", "Event 3")));
    }

    @Test
    public void findAllEvents_Size_Test() {
        assertEquals(2, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );
    }

    @Test
    public void findAllEvents_ItemsNotNull_Test() {
        Objects.requireNonNull(this.eventService.getAllEvents())
            .doOnEach(Assert::assertNotNull);
    }

    @Test
    public void findAllEvents_Dto_Test() {
        assertEquals(this.testEventDtos.toStream().collect(Collectors.toList()),
            Objects.requireNonNull(this.eventService.getAllEvents().toStream().collect(Collectors.toList()))
        );
    }

    @Test
    public void removeEvent_Test() {
        // given
        Mono<EventCommandDto> dto = Mono.just(new EventCommandDto("1", "Event 1", "1"));
        assertEquals(2, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );

        // when
        Mono<Void> result = this.eventService.removeEvent(dto);

        // then
        assertEquals(1, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );
    }

    @Test
    public void createEvent_Test() {
        // given
        EventCommandDto commandDto = new EventCommandDto("3", "Event 3", "1");
        EventQueryDto queryDto = new EventQueryDto("3", "Event 3");

        // when
        Mono<EventQueryDto> result = this.eventService.createEvent(Mono.just(commandDto));

        // then
        assertEquals(queryDto, result.block());
    }

}
