package com.github.repos.viewer.viewer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GitHubApiResponse {
    private String name;
    private Owner owner;
    private List<GitHubBranch> branches;
    private boolean fork;

    @JsonProperty("branches_url")
    private String branchesUrl;

    @Data
    public static class Owner {
        private String login;
    }

    @Data
    public static class GitHubBranch {
        private String name;
        private Commit commit;

        @Data
        public static class Commit {
            private String sha;
        }
    }
}
