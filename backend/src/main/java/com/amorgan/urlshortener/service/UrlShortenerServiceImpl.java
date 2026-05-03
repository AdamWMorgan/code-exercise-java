package com.amorgan.urlshortener.service;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
import com.amorgan.urlshortener.exception.AliasNotFoundException;
import com.amorgan.urlshortener.model.UrlMapping;
import com.amorgan.urlshortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * UrlShortenerServiceImpl.
 * <p>
 * Implementation of the {@link UrlShortenerService} interface.
 * </p>
 *
 * @author adam.morgan
 */

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Value("${app.short-url.base-path}")
    private String baseUrl;

    private final UrlMappingRepository repository;

    @Override
    public String shortenUrl(final ShortenPostRequest request) {
        final String alias = (request.getCustomAlias() != null && !request.getCustomAlias().isBlank())
                ? request.getCustomAlias() : generateRandomAlias();

        if (repository.existsByCustomAlias(alias)) {
            throw new RuntimeException("Alias already exists");
        }

        final String shortUrlValue = baseUrl + alias;

        final UrlMapping mapping = UrlMapping.builder()
                .customAlias(alias)
                .fullUrl(request.getFullUrl())
                .shortUrl(shortUrlValue)
                .build();

        repository.save(mapping);
        return mapping.getShortUrl();
    }

    @Override
    public String getFullUrl(final String alias) {
        return repository.findByCustomAlias(alias)
                .map(UrlMapping::getFullUrl)
                .orElseThrow(() -> new AliasNotFoundException(alias));
    }

    @Override
    public List<UrlsGet200ResponseInner> getAllUrls() {
        return repository.findAll().stream()
                .map(mapping ->
                        new UrlsGet200ResponseInner()
                                .alias(mapping.getCustomAlias())
                                .fullUrl(mapping.getFullUrl())
                                .shortUrl(mapping.getShortUrl()))
                .toList();
    }

    @Override
    public void deleteAlias(final String alias) {
        if (!repository.existsByCustomAlias(alias)) {
            throw new AliasNotFoundException(alias);
        }
        repository.deleteByCustomAlias(alias);
    }

    private String generateRandomAlias() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
