package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Log4j2
@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Flux<EventQueryDto> getAllEvents() {
        return Flux.fromIterable(this.eventRepository.findAll())
            .map(this::getDto);
    }

    public Mono<Long> removeEvent(Mono<EventCommandDto> dto) {
        return dto
            .map(this::getEvent)
            .map(e -> {
                this.eventRepository.deleteById(e.getId());
                return e.getId();
            });
    }

    public Mono<EventQueryDto> createEvent(Mono<EventCommandDto> dto) {
        Mono<Event> event = dto
            .map(this::getEvent)
            .map(e -> new Event(null, e.getEventName()));
        return this.create(event);
    }

    private EventQueryDto getDto(Event event) {
        return new EventQueryDto(event.getId(), event.getEventName());
    }

    private Event getEvent(EventCommandDto command) {
        return new Event(command.getEventId(), command.getEventName());
    }

    private Mono<EventQueryDto> create(Mono<Event> dto) {
        return dto.flatMap(event ->
            Mono.just(this.eventRepository.save(event))
                .map(this::getDto));
    }
}
