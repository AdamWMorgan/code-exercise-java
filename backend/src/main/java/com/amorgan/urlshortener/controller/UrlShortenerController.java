package com.amorgan.urlshortener.controller;

import com.amorgan.urlshortener.api.AliasApi;
import com.amorgan.urlshortener.api.ShortenApi;
import com.amorgan.urlshortener.api.UrlsApi;
import com.amorgan.urlshortener.dto.ShortenPost201Response;
import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
import com.amorgan.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * UrlShortenerController.
 * <p>
 * This controller implements the interfaces generated from the OpenAPI specification,
 * providing endpoints to create, retrieve, list, and delete shortened URLs.
 * </p>
 *
 * @author adam.morgan
 */
@RestController
@RequiredArgsConstructor
public class UrlShortenerController implements ShortenApi, UrlsApi, AliasApi {

    private final UrlShortenerService urlShortenerService;

    /**
     * Provides access to the current native web request.
     * Overridden to resolve conflicts between multiple generated API interfaces.
     *
     * @return an empty Optional as manual request handling is not required.
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * Shortens a provided URL.
     *
     * @param shortenPostRequest the request object containing the full URL and optional custom alias.
     * @return a {@link ResponseEntity} containing the shortened URL and HTTP 201 status.
     */
    @Override
    public ResponseEntity<ShortenPost201Response> shortenPost(@Valid final ShortenPostRequest shortenPostRequest) {
        final String alias = urlShortenerService.shortenUrl(shortenPostRequest);

        final ShortenPost201Response response = new ShortenPost201Response()
                .shortUrl("http://" + alias);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves the original URL associated with the given alias and performs a redirect.
     *
     * @param alias the unique identifier for the shortened URL.
     * @return a {@link ResponseEntity} with HTTP 302 status and the Location header set.
     */
    @Override
    public ResponseEntity<Void> aliasGet(final String alias) {
        final String fullUrl = urlShortenerService.getFullUrl(alias);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(fullUrl))
                .build();
    }

    /**
     * Deletes the mapping for a specific shortened URL alias.
     *
     * @param alias the unique identifier for the shortened URL to be removed.
     * @return a {@link ResponseEntity} with HTTP 204 status on successful deletion.
     */
    @Override
    public ResponseEntity<Void> aliasDelete(final String alias) {
        urlShortenerService.deleteAlias(alias);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a list of all currently shortened URLs.
     *
     * @return a {@link ResponseEntity} containing a list of {@link UrlsGet200ResponseInner} objects.
     */
    @Override
    public ResponseEntity<List<UrlsGet200ResponseInner>> urlsGet() {
        final List<UrlsGet200ResponseInner> urls = urlShortenerService.getAllUrls();
        return ResponseEntity.ok(urls);
    }
}