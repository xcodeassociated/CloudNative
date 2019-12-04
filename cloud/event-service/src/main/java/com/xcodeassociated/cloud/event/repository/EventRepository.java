package com.xcodeassociated.cloud.event.repository;

import com.xcodeassociated.cloud.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
