package com.amorgan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
/**
 * GlobalExceptionHandler.
 * <p>
 * A global exception handler with the purpose of intercepting exceptions thrown by controllers.
 * </p>
 *
 * @author adam.morgan
 */
@ControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(AliasNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAliasNotFoundException(final AliasNotFoundException ex) {
        final HttpStatus status = HttpStatus.NOT_FOUND;

        final ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception ex) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        final ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "An unexpected error occurred"
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        final ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "Malformed JSON request"
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}