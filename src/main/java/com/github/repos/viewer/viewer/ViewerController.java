package com.github.repos.viewer.viewer;

import com.github.repos.viewer.viewer.payload.ViewerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/users")
public class ViewerController {

    private final ViewerService viewerService;

    public ViewerController(ViewerService viewerService) {
        this.viewerService = viewerService;
    }

    @GetMapping("/{username}/repos")
    public Flux<ViewerResponse> getUserRepositories(@PathVariable String username) {
        return viewerService.getUserRepositories(username);
    }

}
