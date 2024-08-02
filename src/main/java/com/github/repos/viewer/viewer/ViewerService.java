package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.ViewerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class ViewerService {

    private static final String GET_BRANCHES_URL_ERROR = "Branches URL is null for repository: ";

    private final WebClient webClient;
    private final ViewerMapper viewerMapper;

    public ViewerService(WebClient webClient, ViewerMapper viewerMapper) {
        this.webClient = webClient;
        this.viewerMapper = viewerMapper;
    }

    public Flux<ViewerResponse> getUserRepositories(String username) {
        return this.webClient
                // Configure WebClient to send a GET request to the specified URI.
                .get()
                .uri("/users/{username}/repos", username)
                // Retrieve the response and covert it to GitHubApiResponse.
                .retrieve()
                .bodyToFlux(GitHubApiResponse.class)
                // Log if branches_url is null. Then we won't be able to retrieve the branches.
                .doOnNext(repo -> {
                    if (repo.getBranchesUrl() == null) {
                        log.warn(GET_BRANCHES_URL_ERROR + repo.getName());
                    }
                })
                .filter(repo -> !repo.isFork())
                .flatMap(this::getRepositoryBranches)
                .map(viewerMapper::mapToViewerResponse);
    }

    private Mono<GitHubApiResponse> getRepositoryBranches(GitHubApiResponse repository) {
        return Optional.ofNullable(repository.getBranchesUrl())
                .map(url -> url.replace("{/branch}", ""))
                .map(url -> this.webClient
                        .get()
                        .uri(url)
                        .retrieve()
                        .bodyToFlux(GitHubApiResponse.GitHubBranch.class)
                        .collectList()
                        .doOnNext(repository::setBranches)
                        // Return the instance of GitHubApiResponse with updated branches data.
                        .thenReturn(repository))
                .orElse(Mono.just(repository));
    }

}
