package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.ViewerResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViewerMapper {

    ViewerResponse mapToViewerResponse(GitHubApiResponse response) {
        ViewerResponse viewerResponse = new ViewerResponse();

        // Set the name of the repository.
        viewerResponse.setName(response.getName());

        // Set the name of the repository owner.
        ViewerResponse.Owner owner = new ViewerResponse.Owner();
        owner.setLogin(response.getOwner().getLogin());
        viewerResponse.setOwner(owner);

        // Set the data for the branches.
        if (response.getBranches() != null) { // To avoid NullPointerException.
            List<ViewerResponse.Branch> branches = response.getBranches().stream()
                    .map(gitHubBranch -> {
                        ViewerResponse.Branch branch = new ViewerResponse.Branch();
                        branch.setName(gitHubBranch.getName());
                        branch.setLastCommitSha(gitHubBranch.getCommit().getSha());
                        return branch;
                    })
                    .toList();
            viewerResponse.setBranches(branches);
        }

        return viewerResponse;
    }

}
