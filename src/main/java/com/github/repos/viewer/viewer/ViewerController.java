package com.github.repos.viewer.viewer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class ViewerController {

    private final ViewerService viewerService;

    public ViewerController(ViewerService viewerService) {
        this.viewerService = viewerService;
    }

    @GetMapping("/{username}/repos")
    public void getUserRepositories(@PathVariable String username) {
        //TODO: call method from ViewerService. Return ViewerResponse.
    }

}
