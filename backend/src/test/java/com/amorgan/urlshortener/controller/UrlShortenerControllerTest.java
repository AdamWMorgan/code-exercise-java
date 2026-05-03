package com.amorgan.urlshortener.controller;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import com.amorgan.urlshortener.dto.UrlsGet200ResponseInner;
import com.amorgan.urlshortener.exception.AliasNotFoundException;
import com.amorgan.urlshortener.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * UrlShortenerControllerTest
 *
 * @author adam.morgan
 */
@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    public static final String ALIAS_PATH_VARIABLE = "/{alias}";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Test
    @DisplayName("Performing a POST to the /shorten endpoint should return 201 and the short URL from the service")
    void testShortenPostReturns201AndCorrectUrl() throws Exception {
        final String mockShortUrl = "https://custom-alias";
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setFullUrl("https://some-long-test-url.com/path");

        when(urlShortenerService.shortenUrl(any(ShortenPostRequest.class))).thenReturn(mockShortUrl);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value(mockShortUrl));

        verify(urlShortenerService, times(1)).shortenUrl(any(ShortenPostRequest.class));
    }

    @Test
    @DisplayName("Performing a GET on the /{alias} endpoint should return 302 redirect to the full URL")
    void testAliasGetReturns302AndRedirect() throws Exception {
        final String alias = "testAlias";
        final String fullUrl = "https://google.com";
        when(urlShortenerService.getFullUrl(alias)).thenReturn(fullUrl);

        mockMvc.perform(get(ALIAS_PATH_VARIABLE, alias))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", fullUrl));

        verify(urlShortenerService, times(1)).getFullUrl(alias);
    }

    @Test
    @DisplayName("Performing a GET on the /{alias} endpoint with a non-existent alias should return 404 Not Found")
    void testAliasGetReturns404WhenAliasNotFound() throws Exception {
        final String alias = "unknownAlias";
        when(urlShortenerService.getFullUrl(alias)).thenThrow(new AliasNotFoundException(alias));

        mockMvc.perform(get(ALIAS_PATH_VARIABLE, alias))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Alias 'unknownAlias' not found"));

        verify(urlShortenerService, times(1)).getFullUrl(alias);
    }

    @Test
    @DisplayName("Performing a DELETE on the /{alias} endpoint should return 204 No Content")
    void testDeleteAliasIsInvokedExactlyOnceAndReturnsNoContent() throws Exception {
        final String alias = "deleteMe";
        doNothing().when(urlShortenerService).deleteAlias(alias);

        mockMvc.perform(delete(ALIAS_PATH_VARIABLE, alias))
                .andExpect(status().isNoContent());

        verify(urlShortenerService, times(1)).deleteAlias(alias);
    }

    @Test
    @DisplayName("Performing a GET on the /urls endpoint should return 200 and list of URL mappings")
    void testGetUrlsReturns200AndListsUrlMappings() throws Exception {
        final UrlsGet200ResponseInner item = new UrlsGet200ResponseInner();

        final String testAlias = "abc";
        item.setAlias(testAlias);

        final List<UrlsGet200ResponseInner> mockList = List.of(item);

        when(urlShortenerService.getAllUrls()).thenReturn(mockList);

        mockMvc.perform(get("/urls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].alias").value(testAlias));

        verify(urlShortenerService, times(1)).getAllUrls();
    }

    @Test
    @DisplayName("Performing a POST to the /shorten endpoint with malformed JSON should return 400 Bad Request")
    void testShortenPostReturns400ForMalformedJson() throws Exception {
        final String malformedJson = "{ \"fullUrl\": ";

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(urlShortenerService);
    }

    @Test
    @DisplayName("Invoking getRequest should return empty optional")
    void getRequestReturnsEmpty() {
        final UrlShortenerController controller = new UrlShortenerController(urlShortenerService);
        assert (controller.getRequest().isEmpty());
    }
}