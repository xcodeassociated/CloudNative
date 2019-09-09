package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import reactor.core.publisher.Mono;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private EventQueryDto getDto(Event event) {
        EventQueryDto dto = new EventQueryDto(event.getId(), event.getEventName());
        // todo: linking...
        return dto;
    }

    public Flux<EventQueryDto> getAllEvents() {
        return this.eventRepository.findAll().map(this::getDto);
    }

    public Mono<Void> removeEvent(EventCommandDto dto) {
        return this.eventRepository.deleteById(dto.getId());
    }

    public Mono<Event> createEvent(EventCommandDto dto) {
        return this.eventRepository.save(new Event(null, dto.getEventName()));
    }
}
