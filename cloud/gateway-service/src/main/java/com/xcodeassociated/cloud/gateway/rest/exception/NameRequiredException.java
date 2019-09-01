package com.xcodeassociated.cloud.gateway.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NameRequiredException extends ResponseStatusException {
    public NameRequiredException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}