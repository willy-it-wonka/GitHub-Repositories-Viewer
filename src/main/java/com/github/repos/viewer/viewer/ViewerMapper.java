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
        ViewerResponse.Owner owner = new ViewerResponse.Owner(response.owner().login());

        // Set the data for the branches.
        List<ViewerResponse.Branch> branches = Collections.emptyList();
        if (response.branches() != null) { // To avoid NullPointerException.
            branches = response.branches().stream()
                    .map(branch -> new ViewerResponse.Branch(branch.name(), branch.commit().sha()))
                    .toList();
        }

        // Set the name of the repository and return ViewerResponse.
        return new ViewerResponse(response.name(), owner, branches);
    }

}
