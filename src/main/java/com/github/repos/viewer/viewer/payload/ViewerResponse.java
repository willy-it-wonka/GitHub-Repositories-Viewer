package com.github.repos.viewer.viewer.payload;

import java.util.List;

public record ViewerResponse(
        String name,
        Owner owner,
        List<Branch> branches
) {

    public record Owner(String login) {
    }

    public record Branch(String name, String lastCommitSha) {
    }

}