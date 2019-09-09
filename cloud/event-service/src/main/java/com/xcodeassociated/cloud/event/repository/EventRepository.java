package com.xcodeassociated.cloud.event.repository;

import com.xcodeassociated.cloud.event.model.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EventRepository extends ReactiveMongoRepository<Event, String> {}
