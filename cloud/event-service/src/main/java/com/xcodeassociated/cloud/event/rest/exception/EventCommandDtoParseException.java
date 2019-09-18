package com.xcodeassociated.cloud.event.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventCommandDtoParseException extends ResponseStatusException {

    public EventCommandDtoParseException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}
