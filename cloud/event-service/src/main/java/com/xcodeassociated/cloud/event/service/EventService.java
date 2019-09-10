package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.dto.EventCommandDto;
import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.xcodeassociated.cloud.event.dto.EventQueryDto;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private EventQueryDto getDto(Event event) {
        EventQueryDto dto = new EventQueryDto(event.getId(), event.getEventName());
        // todo: linking - event with user
        return dto;
    }

    public Flux<EventQueryDto> getAllEvents() {
        return this.eventRepository.findAll().map(this::getDto);
    }

    public void removeEvent(EventCommandDto dto) {
        this.eventRepository.deleteById(dto.getId());
    }

    public Mono<Event> createEvent(EventCommandDto dto) {
        return this.eventRepository.save(new Event(null, dto.getEventName()));
    }
}
