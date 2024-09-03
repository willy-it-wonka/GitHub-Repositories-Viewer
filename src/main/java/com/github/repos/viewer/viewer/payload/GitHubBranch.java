package com.github.repos.viewer.viewer.payload;

public record GitHubBranch(
        String name,
        Commit commit) {
}
