package com.amorgan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AliasNotFoundException
 * <p>
 * Exception thrown when a requested alias does not exist.
 * </p>
 *
 * @author adam.morgan
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class AliasNotFoundException extends RuntimeException {

    public AliasNotFoundException(final String alias) {
        super(String.format("Alias '%s' not found", alias));
    }
}