package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.GitHubBranch;
import com.github.repos.viewer.viewer.payload.ViewerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return fetchUserRepositories(username)
                .flatMap(this::enrichRepositoriesWithBranches)
                .map(viewerMapper::mapToViewerResponse);
    }

    private Flux<GitHubApiResponse> fetchUserRepositories(String username) {
        return this.webClient
                .get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GitHubApiResponse.class)
                .filter(repo -> !repo.fork());
    }

    private Mono<GitHubApiResponse> enrichRepositoriesWithBranches(GitHubApiResponse repository) {
        if (repository.branchesUrl() == null) {
            log.warn(GET_BRANCHES_URL_ERROR + repository.name());
            return Mono.just(repository);
        }

        String branchesUrl = repository.branchesUrl().replace("{/branch}", "");
        return this.webClient
                .get()
                .uri(branchesUrl)
                .retrieve()
                .bodyToFlux(GitHubBranch.class)
                .collectList()
                .map(branches -> new GitHubApiResponse(
                        repository.name(),
                        repository.owner(),
                        branches,
                        repository.fork(),
                        repository.branchesUrl()
                ));
    }

}
