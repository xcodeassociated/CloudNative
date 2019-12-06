package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
public class EventServiceTest {

    @Autowired
    private EventRepository eventRepository;

    private EventService eventService = null;

    private List<EventQueryDto> testEventDtos;

    @Before
    public void setUp() {
        this.eventService = new EventService(this.eventRepository);

        List<Event> testEvents = Arrays.asList(
            new Event(1L, "Event 1"),
            new Event(2L, "Event 2")
        );

        this.testEventDtos = testEvents.stream()
            .map(e -> new EventQueryDto(e.getId(), e.getEventName()))
            .collect(Collectors.toList());

        this.eventRepository.saveAll(testEvents);
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
        assertEquals(this.testEventDtos,
            Objects.requireNonNull(this.eventService.getAllEvents().toStream().collect(Collectors.toList()))
        );
    }

    @Test
    public void removeEvent_Test() {
        // given
        EventQueryDto eventQuery = this.eventService.getAllEvents()
            .take(1)
            .blockFirst();

        assert eventQuery != null;
        EventCommandDto dto = new EventCommandDto(eventQuery.getEventId(), eventQuery.getEventName(), 1L);

        assertEquals(2, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );

        // when
        Mono<Long> result = this.eventService.removeEvent(Mono.just(dto));
        result.block();

        // then
        assertEquals(1, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );
    }

    @Test
    public void createEvent_Test() {
        // given
        EventCommandDto commandDto = new EventCommandDto(3L, "Event 3", 1L);
        EventQueryDto queryDto = new EventQueryDto(3L, "Event 3");
        assertEquals(2, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );

        // when
        Mono<EventQueryDto> result = this.eventService.createEvent(Mono.just(commandDto));

        // then
        assertEquals(queryDto, result.block());
        assertEquals(3, Objects.requireNonNull
            (this.eventService.getAllEvents().count().block()).intValue()
        );
    }

}
