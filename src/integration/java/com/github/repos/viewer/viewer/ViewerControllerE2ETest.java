package com.github.repos.viewer.viewer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ViewerControllerE2ETest {

    private static final String USER_REPOS_URL = "/users/username/repos";
    private static final String REPO1_BRANCHES_URL = "/repos/username/repo1/branches";
    private static final String REPO2_BRANCHES_URL = "/repos/username/repo2/branches";
    private static final String REPO1_JSON = """
            [{
                "name": "repo1",
                "owner": {
                    "login": "username"
                },
                "branches_url": "/repos/username/repo1/branches",
                "fork": false
            }]
            """;
    private static final String REPO2_JSON = """
            [{
                "name": "repo2",
                "owner": {
                    "login": "username"
                },
                "branches_url": "/repos/username/repo2/branches",
                "fork": true
            }]
            """;
    private static final String BRANCH1_JSON = """
            [{
                "name": "main",
                "commit": {
                    "sha": "commitsha1"
                }
            }]
            """;
    private static final String BRANCH2_JSON = """
            [{
                "name": "main",
                "commit": {
                    "sha": "commitsha2"
                }
            }]
            """;

    private static WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void setUpBeforeAll() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.url", wireMockServer::baseUrl);
    }

    @AfterAll
    static void tearDownAfterAll() {
        wireMockServer.stop();
    }

    // Mocks response for repositories and branches.
    static void mockResponse(String url, String responseBody) {
        WireMock.stubFor(get(url).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));
    }


    @Test
    void whenNoRepositories_thenReturnEmptyList() {
        mockResponse(USER_REPOS_URL, "[]");

        webTestClient.get().uri("/api/v1/users/username/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    void whenRepositoryIsNotFork_thenReturnRepository() {
        mockResponse(USER_REPOS_URL, REPO1_JSON);
        mockResponse(REPO1_BRANCHES_URL, BRANCH1_JSON);

        webTestClient.get().uri("/api/v1/users/username/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("repo1")
                .jsonPath("$[0].owner.login").isEqualTo("username")
                .jsonPath("$[0].branches[0].name").isEqualTo("main")
                .jsonPath("$[0].branches[0].lastCommitSha").isEqualTo("commitsha1");
    }

    @Test
    void whenRepositoryIsFork_thenReturnEmptyList() {
        mockResponse(USER_REPOS_URL, REPO2_JSON);

        webTestClient.get().uri("/api/v1/users/username/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    void whenNoBranchesInRepository_thenReturnRepositoryWithNoBranches() {
        mockResponse(USER_REPOS_URL, REPO1_JSON);
        mockResponse(REPO1_BRANCHES_URL, "[]");

        webTestClient.get().uri("/api/v1/users/username/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("repo1")
                .jsonPath("$[0].owner.login").isEqualTo("username")
                .jsonPath("$[0].branches").isEmpty();
    }

    @Test
    void whenMultipleRepositories_thenReturnAllRepositories() {
        mockResponse(USER_REPOS_URL, """
                [{
                    "name": "repo1",
                    "owner": {
                        "login": "username"
                    },
                    "branches_url": "/repos/username/repo1/branches",
                    "fork": false
                }, {
                    "name": "repo2",
                    "owner": {
                        "login": "username"
                    },
                    "branches_url": "/repos/username/repo2/branches",
                    "fork": false
                }]
                """);
        mockResponse(REPO1_BRANCHES_URL, BRANCH1_JSON);
        mockResponse(REPO2_BRANCHES_URL, BRANCH2_JSON);

        webTestClient.get().uri("/api/v1/users/username/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].owner.login").isEqualTo("username")
                .jsonPath("$[0].name").isEqualTo("repo1")
                .jsonPath("$[0].branches[0].name").isEqualTo("main")
                .jsonPath("$[0].branches[0].lastCommitSha").isEqualTo("commitsha1")
                .jsonPath("$[1].name").isEqualTo("repo2")
                .jsonPath("$[1].branches[0].name").isEqualTo("main")
                .jsonPath("$[1].branches[0].lastCommitSha").isEqualTo("commitsha2");
    }

}
