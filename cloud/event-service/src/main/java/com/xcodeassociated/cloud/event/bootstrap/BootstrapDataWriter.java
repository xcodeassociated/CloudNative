package com.xcodeassociated.cloud.event.bootstrap;

import com.xcodeassociated.cloud.event.model.Event;
import com.xcodeassociated.cloud.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Log4j2
@Component
@Profile("dev")
public class BootstrapDataWriter implements ApplicationRunner {
    private final EventRepository eventRepository;

    public BootstrapDataWriter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataWriter Bean Context Started");
        this.eventRepository
                .deleteAll()
                .thenMany(
                        Flux.just("Event 1", "Event 2", "Event 3", "Event 4", "Event 5")
                                .map(name -> new Event(null, name))
                                .flatMap(this.eventRepository::save))
                .thenMany(this.eventRepository.findAll())
                .subscribe(log::debug);
    }
}
