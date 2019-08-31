package com.xcodeassociated.cloud.event.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ResourceDto extends ResourceSupport {

    private final String content;

    @JsonCreator
    public ResourceDto(@JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}