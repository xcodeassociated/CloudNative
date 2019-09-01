package com.xcodeassociated.cloud.event.bootstrap;

import com.xcodeassociated.cloud.event.model.Reservation;
import com.xcodeassociated.cloud.event.repository.ReservationRepository;
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
    private final ReservationRepository reservationRepository;

    public BootstrapDataWriter(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataWriter Bean Context Started");
        this.reservationRepository
                .deleteAll()
                .thenMany(
                        Flux.just("Alice", "Bob", "Mike", "Steve")
                                .map(name -> new Reservation(null, name))
                                .flatMap(this.reservationRepository::save))
                .thenMany(this.reservationRepository.findAll())
                .subscribe(log::debug);
    }
}