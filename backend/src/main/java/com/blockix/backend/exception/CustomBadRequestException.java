package com.blockix.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomBadRequestException extends RuntimeException {
    private final Map<String, Object> errors = new HashMap<>();
    private final int httpStatusCode = HttpStatus.BAD_REQUEST.value();

    public CustomBadRequestException(String message) {
        super(message);
        this.errors.put("message", message);
        this.initErrors();
    }

    public CustomBadRequestException(String message, Map<String, Object> errors) {
        this(message);
        this.errors.putAll(errors);
    }

    private void initErrors() {
        this.errors.put("status", this.httpStatusCode);
    }
}
