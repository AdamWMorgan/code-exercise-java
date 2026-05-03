Feature: URL Shortener API Basics

  Background:
    Given the API is up and running

  Scenario: Successfully shorten a URL with a custom alias
    When I request to shorten "https://www.google.com" with custom alias "my-custom-link"
    Then the response status should be 201
    And I should receive a short URL containing "my-custom-link"

  Scenario: Create and use a custom alias for redirection
    When I request to shorten "https://github.com" with custom alias "my-git"
    And I follow the redirect for alias "my-git"
    Then the full URL should be "https://github.com"

  Scenario: Handle non-existent alias redirect
    When I follow the redirect for alias "this-does-not-exist"
    Then the response status should be 404

  Scenario: Retrieve all shortened URLs
    When I request to shorten the URL "https://www.bbc.com"
    And I request all shortened URLs
    Then the response status should be 200
    And the list should contain at least 1 mapping

  Scenario: Delete a shortened URL mapping
    When I request to shorten "https://example.com" with custom alias "to-delete"
    And I delete the alias "to-delete"
    And I follow the redirect for alias "to-delete"
    Then the response status should be 404

  Scenario: Prevent duplicate custom aliases
    When I request to shorten "https://test.com" with custom alias "dupe"
    And I request to shorten "https://other.com" with custom alias "dupe"
    Then the response status should be 500