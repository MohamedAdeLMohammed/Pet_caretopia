package com.PetCaretopia.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle all RuntimeExceptions (like user not found, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle async CompletionException from CompletableFuture
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<?> handleCompletionException(CompletionException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    // Default fallback for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return buildErrorResponse("Something went wrong. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("status", status.value());
        error.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(error, status);
    }
}
