package com.amorgan.urlshortener.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * AliasNotFoundExceptionTest
 *
 * @author adam.morgan
 */
@ExtendWith(MockitoExtension.class)
class AliasNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor should correctly format the exception message with the provided alias")
    void constructorSetsFormattedMessage() {
        final String alias = "missing123";
        final AliasNotFoundException exception = new AliasNotFoundException(alias);

        assertEquals("Alias 'missing123' not found", exception.getMessage());
    }

    @Test
    @DisplayName("Exception class should be annotated with ResponseStatus NOT_FOUND")
    void exceptionHasCorrectResponseStatusAnnotation() {
        final ResponseStatus annotation = AliasNotFoundException.class.getAnnotation(ResponseStatus.class);

        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }
}