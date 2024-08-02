package com.github.repos.viewer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_VALUE = "application/json";

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(GITHUB_API_URL)
                .defaultHeader(HEADER_ACCEPT, HEADER_ACCEPT_VALUE)
                .build();
    }

}
