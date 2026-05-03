package com.amorgan.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UrlShortenerApplication.
 * <p>
 * This class initialises the Spring Boot application context, performs auto-configuration,
 * and starts the embedded web server.
 * </p>
 *
 * @author adam.morgan
 */
@SpringBootApplication
public class UrlShortenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
}
