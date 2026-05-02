package com.amorgan.urlshortener.service;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
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
public class UrlShortenerServiceImpl implements UrlShortenerService {
    @Override
    public String shortenUrl(final ShortenPostRequest request) {
        final String alias = (request.getCustomAlias() != null && !request.getCustomAlias().isBlank())
                ? request.getCustomAlias()
                : generateRandomAlias();

        return alias;
    }

    @Override
    public String getFullUrl(final String alias) {
        return "https://google.com";
    }

    @Override
    public List<UrlsGet200ResponseInner> getAllUrls() {
        return List.of();
    }

    @Override
    public void deleteAlias(final String alias) {
    }

    /**
     * Generates a random string to use as the alias.
     */
    private String generateRandomAlias() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
