package com.example.studentmanagement.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        return response(HttpStatus.CONFLICT, ex.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Request validation failed", errors);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraint(Exception ex) {
        return response(HttpStatus.CONFLICT,
                "A database constraint was violated; check duplicate values and input data.",
                Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex) {
        return response(HttpStatus.BAD_REQUEST, "Request body is missing or malformed JSON", Map.of());
    }

    private ResponseEntity<ApiError> response(
            HttpStatus status, String message, Map<String, String> validationErrors) {
        ApiError body = new ApiError(Instant.now(), status.value(),
                status.getReasonPhrase(), message, validationErrors);
        return ResponseEntity.status(status).body(body);
    }
}
