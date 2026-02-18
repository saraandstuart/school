package com.shannoncode.school.controller;

import java.nio.file.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access Denied",
            "You are not enrolled in this course. Please sign up to view its content."
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // A simple DTO for the error body
    public record ErrorResponse(int status, String error, String message) {
    }
}
