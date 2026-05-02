package com.amorgan.urlshortener.service;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
import com.amorgan.urlshortener.exception.AliasNotFoundException;
import com.amorgan.urlshortener.model.UrlMapping;
import com.amorgan.urlshortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceImplTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    private final String baseUrl = "http://localhost:8080/";

    @BeforeEach
    void setUp() {
        // Using reflection to inject the baseUrl config value to avoid unnecessary startup of Spring context.
        ReflectionTestUtils.setField(urlShortenerService, "baseUrl", baseUrl);
    }

    @Test
    @DisplayName("shortenUrl should save and return short URL when custom alias is provided")
    void testShortenUrlHasSavedExactlyOnceAndReturnsShortUrl() {
        final String customAlias = "my-alias";
        final String fullUrl = "https://google.com";
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setCustomAlias(customAlias);
        request.setFullUrl(fullUrl);

        when(repository.existsByCustomAlias(customAlias)).thenReturn(false);

        final String result = urlShortenerService.shortenUrl(request);

        assertThat(result).isEqualTo(baseUrl + customAlias);
        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    @DisplayName("shortenUrl should generate a random alias when custom alias is null")
    void testShortenUrlWithRandomAliasGenerates8CharacterRandomAliasAndInvokesSaveExactlyOnce() {
        final String fullUrl = "https://google.com";
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setFullUrl(fullUrl);
        // Forcing generation of random alias
        request.setCustomAlias(null);

        when(repository.existsByCustomAlias(anyString())).thenReturn(false);

        final String result = urlShortenerService.shortenUrl(request);

        assertThat(result).startsWith(baseUrl);
        final String generatedAlias = result.replace(baseUrl, "");
        assertThat(generatedAlias).hasSize(8);

        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    @DisplayName("shortenUrl should throw exception when alias already exists")
    void testShortenUrlThrowsExceptionWhenAliasAlreadyExists() {
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setCustomAlias("exists");

        when(repository.existsByCustomAlias("exists")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> urlShortenerService.shortenUrl(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("getFullUrl should return the long URL for a valid alias")
    void testGetFullUrlReturnsLongUrlForValidAlias() {
        final String alias = "goo";
        final String fullUrl = "https://google.com";
        final UrlMapping mapping = UrlMapping.builder()
                .customAlias(alias)
                .fullUrl(fullUrl)
                .build();

        when(repository.findByCustomAlias(alias)).thenReturn(Optional.of(mapping));

        final String result = urlShortenerService.getFullUrl(alias);

        assertThat(result).isEqualTo(fullUrl);
    }

    @Test
    @DisplayName("getFullUrl should throw AliasNotFoundException when alias is missing")
    void testGetFullUrlThrowsAliasNotFoundExceptionWhenThereIsNoMatchingAlias() {
        when(repository.findByCustomAlias("missing")).thenReturn(Optional.empty());

        assertThrows(AliasNotFoundException.class, () -> urlShortenerService.getFullUrl("missing"));
    }

    @Test
    @DisplayName("getAllUrls should return a list of UrlsGet200ResponseInner")
    void testGetAllUrlsReturnsListOfUrlsGet200ResponseInner() {
        final UrlMapping mapping = UrlMapping.builder()
                .customAlias("alias1")
                .fullUrl("https://url1.com")
                .shortUrl(baseUrl + "alias1")
                .build();

        when(repository.findAll()).thenReturn(List.of(mapping));

        final List<UrlsGet200ResponseInner> results = urlShortenerService.getAllUrls();

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getShortUrl()).isEqualTo(baseUrl + "alias1");
        assertThat(results.getFirst().getAlias()).isEqualTo("alias1");
    }

    @Test
    @DisplayName("deleteAlias should call repository delete when alias exists")
    void testDeleteAliasInvokedDeleteByCustomAlias() {
        final String alias = "delete-me";
        when(repository.existsByCustomAlias(alias)).thenReturn(true);

        urlShortenerService.deleteAlias(alias);

        verify(repository, times(1)).deleteByCustomAlias(alias);
    }

    @Test
    @DisplayName("deleteAlias should throw AliasNotFoundException when alias does not exist")
    void testDeleteAliasThrowsAliasNotFoundExceptionWhenThereIsNoMatchingAlias() {
        final String alias = "unknown";
        when(repository.existsByCustomAlias(alias)).thenReturn(false);

        assertThrows(AliasNotFoundException.class, () -> urlShortenerService.deleteAlias(alias));
        verify(repository, never()).deleteByCustomAlias(anyString());
    }
}