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
    private Long eventId;
    private String eventName;
    private Long id;

    @JsonCreator
    public EventCommandDto(@JsonProperty("eventId") Long eventId,
                           @JsonProperty("eventName") String eventName,
                           @JsonProperty("id") Long id) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.id = id;
    }
}
