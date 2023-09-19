package com.gwtech.rewards.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.gwtech.rewards.controller.response.ApiError;
import com.gwtech.rewards.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        log.error("{}, {}", ex.getMessage(), status);
        if (ex instanceof HttpMessageNotReadableException) {
            return buildResponseEntity("Invalid JSON message", status);
        }
        return buildResponseEntity(ex.getMessage(), status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error("{}, {}", ex.getMessage(), NOT_FOUND);
        return buildResponseEntity(ex.getMessage(), NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError errorMessage = ApiError.builder()
                .errors(errors)
                .status(status)
                .message("Validation errors")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(errorMessage, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(((MethodArgumentTypeMismatchException)ex).getName(), ex.getMessage());
        ApiError errorMessage = ApiError.builder()
                .errors(errors)
                .status(status)
                .message("Validation errors")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(errorMessage, headers, status);
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .status(status)
                .build();
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
