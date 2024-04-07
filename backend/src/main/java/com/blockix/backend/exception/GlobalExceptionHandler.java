package com.blockix.backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleCustomBadRequestException(CustomBadRequestException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(ex.getErrors());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomNotFoundException(CustomNotFoundException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(ex.getErrors());
    }

    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<Map<String, Object>> handleCustomInternalServerException(CustomInternalServerException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(ex.getErrors());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
