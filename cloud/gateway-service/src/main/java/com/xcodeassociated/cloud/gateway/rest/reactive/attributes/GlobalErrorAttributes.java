package com.xcodeassociated.cloud.gateway.rest.reactive.attributes;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    private HttpStatus status;
    private String message;

    public GlobalErrorAttributes() {
        this(HttpStatus.BAD_REQUEST, "...");
    }

    public GlobalErrorAttributes(HttpStatus status, String message) {
        super(false);
        this.status = status;
        this.message = message;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
        map.put("status", getStatus());
        map.put("message", getMessage());
        return map;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}