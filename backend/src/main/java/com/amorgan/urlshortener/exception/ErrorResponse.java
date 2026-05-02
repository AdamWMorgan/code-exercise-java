package com.amorgan.urlshortener.exception;

import java.time.LocalDateTime;

/**
 * ErrorResponse
 * <p>
 * A record representing a standardised error response.
 * </p>
 *
 * @author adam.morgan
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
}