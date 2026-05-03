package com.amorgan.integration.stepdefinitions;

import com.amorgan.urlshortener.dto.ShortenPostRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * StepDefinitions.
 *
 * @author adam.morgan
 */
public final class StepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<?> latestResponse;

    @Given("the API is up and running")
    public void theApiIsUpAndRunning() {
        // This is handled automatically in the cucumber configuration.
    }

    @When("I request to shorten the URL {string}")
    public void shortenUrl(final String fullUrl) {
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setFullUrl(fullUrl);
        latestResponse = restTemplate.postForEntity("/shorten", request, String.class);
    }

    @When("I request to shorten {string} with custom alias {string}")
    public void shortenUrlWithAlias(final String fullUrl, final String alias) {
        final ShortenPostRequest request = new ShortenPostRequest();
        request.setFullUrl(fullUrl);
        request.setCustomAlias(alias);
        latestResponse = restTemplate.postForEntity("/shorten", request, String.class);
    }

    @When("I follow the redirect for alias {string}")
    public void followRedirect(final String alias) {
        latestResponse = restTemplate.getForEntity("/" + alias, String.class);
    }

    @When("I request all shortened URLs")
    public void getAllUrls() {
        latestResponse = restTemplate.getForEntity("/urls", List.class);
    }

    @When("I delete the alias {string}")
    public void deleteAlias(final String alias) {
        latestResponse = restTemplate.exchange("/" + alias, HttpMethod.DELETE, null, Void.class);
    }

    @Then("the response status should be {int}")
    public void verifyStatus(final int statusCode) {
        assertThat(latestResponse.getStatusCode().value()).isEqualTo(statusCode);
    }

    @Then("I should receive a short URL")
    public void verifyShortUrlProduced() {
        assertThat(latestResponse.getBody().toString()).contains("shortUrl");
    }

    @Then("I should receive a short URL containing {string}")
    public void verifyShortUrlContainsAlias(final String expectedAlias) {
        assertThat(latestResponse.getBody().toString()).contains(expectedAlias);
    }

    @Then("the full URL should be {string}")
    public void verifyFullUrl(final String expectedFullUrl) {
        if (latestResponse.getStatusCode().is5xxServerError()) {
            throw new AssertionError("Server crashed (500). Body: " + latestResponse.getBody());
        }

        if (latestResponse.getStatusCode() == HttpStatus.FOUND) {
            final String actualRedirect = Objects.requireNonNull(latestResponse.getHeaders().getLocation()).toString();
            assertThat(actualRedirect).isEqualTo(expectedFullUrl);
        } else {
            assertTrue(latestResponse.getStatusCode().is2xxSuccessful(),
                    "Expected redirect or success, but got: " + latestResponse.getStatusCode());
        }
    }

    @Then("the list should contain at least {int} mapping")
    public void verifyListSize(final int minSize) {
        assertThat(latestResponse.getBody()).isInstanceOf(List.class);
        final List<?> list = (List<?>) latestResponse.getBody();
        assertThat(list).isNotNull();
        assertTrue(list.size() >= minSize);
    }
}