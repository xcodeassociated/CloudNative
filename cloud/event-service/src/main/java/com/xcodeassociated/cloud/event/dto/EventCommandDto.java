package com.xcodeassociated.cloud.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Data
public class EventCommandDto {
    private String eventId;
    private String eventName;
    private String id;

    @JsonCreator
    public EventCommandDto(@JsonProperty("eventId") String eventId,
                           @JsonProperty("eventName") String eventName,
                           @JsonProperty("id") String id) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.id = id;
    }
}
