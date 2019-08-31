package com.xcodeassociated.cloud.event.service;

import com.xcodeassociated.cloud.event.model.Reservation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {}
