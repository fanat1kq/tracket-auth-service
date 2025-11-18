package ru.example.authservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.authservice.dto.response.ErrorResponse;
import ru.example.authservice.exception.user.UserAlreadyExistsException;
import ru.example.authservice.exception.user.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "AUTHENTICATION_FAILED",
            "Invalid credentials",
            HttpStatus.UNAUTHORIZED.value()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
        UserAlreadyExistsException ex) {
        log.warn("User registration failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "USER_ALREADY_EXISTS",
            "User with this email already exists",
            HttpStatus.CONFLICT.value()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            "User not found",
            HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "VALIDATION_FAILED",
            "Validation failed",
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "CONSTRAINT_VIOLATION",
            "Constraint violation",
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error in auth controller", ex);

        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}