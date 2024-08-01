package com.github.repos.viewer.viewer.payload;

import lombok.Data;

import java.util.List;

@Data
public class ViewerResponse {
    private String name;
    private Owner owner;
    private List<Branch> branches;

    @Data
    public static class Owner {
        private String login;
    }

    @Data
    public static class Branch {
        private String name;
        private String lastCommitSha;
    }
}
