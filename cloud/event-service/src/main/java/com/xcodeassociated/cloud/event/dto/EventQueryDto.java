package com.xcodeassociated.cloud.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
public class EventQueryDto extends ResourceSupport {
    private final String eventId;
    private final String eventName;
    // implicit: Array<Links>

    @JsonCreator
    public EventQueryDto(@JsonProperty("eventId") String eventId,
                         @JsonProperty("eventName") String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }

}