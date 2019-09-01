package com.xcodeassociated.cloud.event.rest;

import com.xcodeassociated.cloud.event.model.Reservation;
import com.xcodeassociated.cloud.event.rest.dto.ResourceDto;
import com.xcodeassociated.cloud.event.repository.ReservationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Log4j2
@RestController
public class RequestHandler {
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/reservations")
    Flux<Reservation> getAll() {
        return this.reservationRepository.findAll();
    }

    @RequestMapping("/links")
    public HttpEntity<ResourceDto> greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        var format = "%s";
        ResourceDto resourceDto = new ResourceDto(String.format(format, name));
        resourceDto.add(linkTo(methodOn(RequestHandler.class).greeting(name)).withSelfRel());

        return new ResponseEntity<>(resourceDto, HttpStatus.OK);
    }
}
