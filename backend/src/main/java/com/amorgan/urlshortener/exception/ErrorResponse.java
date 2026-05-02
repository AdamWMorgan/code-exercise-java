package com.amorgan.urlshortener.exception;

import java.time.OffsetDateTime;

/**
 * ErrorResponse
 * <p>
 * A record representing a standardised error response.
 * </p>
 *
 * @author adam.morgan
 */
public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message
) {
}