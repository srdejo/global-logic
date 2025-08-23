package io.github.srdejo.technical.test.infrastructure.controllers;

import io.github.srdejo.technical.test.application.dtos.ErrorDetail;
import io.github.srdejo.technical.test.application.dtos.ErrorResponse;
import io.github.srdejo.technical.test.domain.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
        var error = new ErrorDetail(LocalDateTime.now(), HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(error)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        var error = new ErrorDetail(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(List.of(error)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> ErrorDetail.builder()
                        .timestamp(LocalDateTime.now())
                        .codigo(HttpStatus.BAD_REQUEST.value())
                        .detail(err.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(ErrorResponse.builder().error(details).build());
    }
}
