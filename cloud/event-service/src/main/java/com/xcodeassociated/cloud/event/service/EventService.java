package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.xcodeassociated.cloud.event.dto.EventDto;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private EventDto getDto(Event event) {
        EventDto dto = new EventDto(event.getId(), event.getEventName());
        return dto;
    }

    public Flux<EventDto> getAllEvents() {
        return this.eventRepository.findAll().map(this::getDto);
    }
}
