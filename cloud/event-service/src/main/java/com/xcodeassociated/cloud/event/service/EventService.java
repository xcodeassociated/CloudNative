package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Log4j2
@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private EventQueryDto getDto(Event event) {
        // todo: linking - event with user
        return new EventQueryDto(event.getId(), event.getEventName());
    }

    private Event getEvent(EventCommandDto command) {
        return new Event(command.getEventId(), command.getEventName());
    }

    public Flux<EventQueryDto> getAllEvents() {
        return this.eventRepository
            .findAll()
            .map(this::getDto);
    }

    public Mono<Void> removeEvent(Mono<EventCommandDto> dto) {
        return dto.map(this::getEvent)
            .flatMap(e ->
                this.eventRepository.deleteById(e.getId()));
    }

    private Mono<EventQueryDto> create(Mono<Event> dto) {
        return dto.flatMap(event ->
            this.eventRepository
                .save(event)
                .map(this::getDto));
    }

    public Mono<EventQueryDto> createEvent(Mono<EventCommandDto> dto) {
        Mono<Event> event = dto
            .map(this::getEvent)
            .map(e -> new Event(null, e.getEventName()));
        return this.create(event);
    }
}
