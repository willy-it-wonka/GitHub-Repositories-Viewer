package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.GitHubApiResponse;
import com.github.repos.viewer.viewer.payload.ViewerResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ViewerMapper {

    ViewerResponse mapToViewerResponse(GitHubApiResponse response) {
        ViewerResponse.Owner owner = new ViewerResponse.Owner(response.owner().login());

        List<ViewerResponse.Branch> branches = Collections.emptyList();
        if (response.branches() != null) {
            branches = response.branches().stream()
                    .map(branch -> new ViewerResponse.Branch(branch.name(), branch.commit().sha()))
                    .toList();
        }

        return new ViewerResponse(response.name(), owner, branches);
    }

}
