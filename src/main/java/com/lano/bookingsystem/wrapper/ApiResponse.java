package com.lano.bookingsystem.wrapper;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = null;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int status, String message, T data, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}