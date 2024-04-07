package com.blockix.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInternalServerException extends RuntimeException {
    private final Map<String, Object> errors = new HashMap<>();
    private final int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public CustomInternalServerException(String message) {
        super(message);
        this.errors.put("message", message);
        this.initErrors();
    }

    public CustomInternalServerException(String message, Map<String, Object> errors) {
        this(message);
        this.errors.putAll(errors);
    }

    private void initErrors() {
        this.errors.put("status", this.httpStatusCode);
    }
}
