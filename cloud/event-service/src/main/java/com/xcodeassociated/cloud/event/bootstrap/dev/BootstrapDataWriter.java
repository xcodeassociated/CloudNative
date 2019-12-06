package com.xcodeassociated.cloud.event.bootstrap.dev;

import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
@Profile({"dev", "local-dev"})
public class BootstrapDataWriter implements ApplicationRunner {
    private final EventRepository eventRepository;

    public BootstrapDataWriter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataWriter Bean Context Started");
        List<Event> events = Arrays.asList(
            new Event(null, "Event 0"),
            new Event(null, "Event 1"));

        this.eventRepository.deleteAll();
        this.eventRepository.saveAll(events);
    }
}
