package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.GitHubBranch;
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
                .get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GitHubApiResponse.class)
                .doOnNext(repo -> {
                    if (repo.branchesUrl() == null) {
                        log.warn(GET_BRANCHES_URL_ERROR + repo.name());
                    }
                })
                .filter(repo -> !repo.fork())
                .flatMap(this::getRepositoryBranches)
                .map(viewerMapper::mapToViewerResponse);
    }

    private Mono<GitHubApiResponse> getRepositoryBranches(GitHubApiResponse repository) {
        return Optional.ofNullable(repository.branchesUrl())
                .map(url -> url.replace("{/branch}", ""))
                .map(url -> this.webClient
                        .get()
                        .uri(url)
                        .retrieve()
                        .bodyToFlux(GitHubBranch.class)
                        .collectList()
                        .map(branches -> new GitHubApiResponse(
                                repository.name(),
                                repository.owner(),
                                branches,
                                repository.fork(),
                                repository.branchesUrl()
                        )))
                .orElse(Mono.just(repository));
    }

}
