package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.ViewerResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ViewerMapper {

    ViewerResponse mapToViewerResponse(GitHubApiResponse response) {
        // Set the name of the repository owner.
        ViewerResponse.Owner owner = new ViewerResponse.Owner(response.getOwner().getLogin());

        // Set the data for the branches.
        List<ViewerResponse.Branch> branches = Collections.emptyList();
        if (response.getBranches() != null) { // To avoid NullPointerException.
            branches = response.getBranches().stream()
                    .map(branch -> new ViewerResponse.Branch(branch.getName(), branch.getCommit().getSha()))
                    .toList();
        }

        // Set the name of the repository and return ViewerResponse.
        return new ViewerResponse(response.getName(), owner, branches);
    }

}
