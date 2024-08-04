package com.github.repos.viewer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_VALUE = "application/json";

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               @Value("${github.api.url}") String githubApiUrl) {
        return builder.baseUrl(githubApiUrl)
                .defaultHeader(HEADER_ACCEPT, HEADER_ACCEPT_VALUE)
                .build();
    }

}
