package com.amorgan.urlshortener.service;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
import com.amorgan.urlshortener.exception.AliasNotFoundException;

import java.util.List;

/**
 * UrlShortenerService.
 * <p>
 * Interface for URL shortening logic.
 * </p>
 *
 * @author adam.morgan
 */
public interface UrlShortenerService {

    /**
     * Shortens a URL based on the request data.
     *
     * @param request The DTO containing the full URL and optional custom alias.
     * @return The generated or assigned alias.
     */
    String shortenUrl(final ShortenPostRequest request);

    /**
     * Retrieves the original full URL for a given alias.
     *
     * @param alias The shortened identifier.
     * @return The original full URL.
     * @throws AliasNotFoundException if the provided alias does not exist in the database.
     */
    String getFullUrl(final String alias);

    /**
     * Retrieves all URL mappings currently in the system.
     *
     * @return A list of all URL mappings.
     */
    List<UrlsGet200ResponseInner> getAllUrls();

    /**
     * Removes a URL mapping.
     *
     * @param alias The alias to delete.
     */
    void deleteAlias(final String alias);
}
