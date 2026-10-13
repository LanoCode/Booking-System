package com.lano.bookingsystem.exception;

import com.lano.bookingsystem.wrapper.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(
                new ApiResponse<>(400, "Validation failed", null, errors)
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(404, ex.getMessage(), null, null)
        );
    }

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {

        log.error("Unexpected error occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(500, "Internal server error", null, null)
        );
    }
    }
