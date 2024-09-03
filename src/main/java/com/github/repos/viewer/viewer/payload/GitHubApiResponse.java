package com.github.repos.viewer.viewer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubApiResponse(
        String name,
        Owner owner,
        List<GitHubBranch> branches,
        boolean fork,
        @JsonProperty("branches_url") String branchesUrl) {
}
