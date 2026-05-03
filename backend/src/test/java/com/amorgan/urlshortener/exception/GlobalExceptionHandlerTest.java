package com.amorgan.urlshortener.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * GlobalExceptionHandlerTest
 *
 * @author adam.morgan
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleAliasNotFoundException should return 404 status and correct body")
    void handleAliasNotFoundExceptionReturnsCorrectResponse() {
        final String alias = "testAlias";
        final AliasNotFoundException ex = new AliasNotFoundException(alias);

        final ResponseEntity<ErrorResponse> response = handler.handleAliasNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("Alias 'testAlias' not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("handleHttpMessageNotReadableException should return 400 status and malformed JSON message")
    void handleHttpMessageNotReadableExceptionReturnsCorrectResponse() {
        final HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        final ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Malformed JSON request", response.getBody().message());
    }

    @Test
    @DisplayName("handleGeneralException should return 500 status and generic message")
    void handleGeneralExceptionReturnsCorrectResponse() {
        final Exception ex = new RuntimeException("Sensitive internal error");

        final ResponseEntity<ErrorResponse> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }
}