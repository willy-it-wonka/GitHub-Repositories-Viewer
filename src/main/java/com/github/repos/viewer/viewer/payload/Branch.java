package com.github.repos.viewer.viewer.payload;

public record Branch(
        String name,
        String lastCommitSha) {
}
