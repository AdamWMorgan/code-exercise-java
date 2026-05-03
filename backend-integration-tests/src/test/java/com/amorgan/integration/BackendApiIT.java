package com.amorgan.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "pretty", "html:target/cucumber", "json:target/cucumber_client.json" },
        features = { "src/test/resources/features" },
        glue = { "com.amorgan.integration" }
)
public class BackendApiIT {
}