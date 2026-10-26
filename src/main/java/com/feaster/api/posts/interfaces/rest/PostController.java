package com.feaster.api.posts.interfaces.rest;

import com.feaster.api.posts.application.commandservices.PostCommandService;
import com.feaster.api.posts.application.queryservices.PostQueryService;
import com.feaster.api.posts.interfaces.rest.resources.PostResource;
import com.feaster.api.posts.interfaces.rest.resources.CreatePostResource;
import com.feaster.api.posts.interfaces.rest.resources.CreateCommentResource;
import com.feaster.api.posts.interfaces.rest.transform.CreatePostCommandFromResourceAssembler;
import com.feaster.api.posts.interfaces.rest.transform.PostResourceFromViewAssembler;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    public PostController(PostCommandService postCommandService, PostQueryService postQueryService) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
    }

    @GetMapping("/posts")
    public List<PostResource> list(@RequestParam(required = false) String q,
                                   @AuthenticationPrincipal String currentUserKey) {
        return postQueryService.list(q, currentUserKey).stream()
                .map(PostResourceFromViewAssembler::toResource).toList();
    }

    @GetMapping("/posts/{postKey}")
    public PostResource find(@PathVariable String postKey, @AuthenticationPrincipal String currentUserKey) {
        return PostResourceFromViewAssembler.toResource(postQueryService.find(postKey, currentUserKey));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResource> create(@AuthenticationPrincipal String currentUserKey,
                                                @Valid @RequestBody CreatePostResource resource) {
        var post = postCommandService.create(currentUserKey,
                CreatePostCommandFromResourceAssembler.toCommand(resource));
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResourceFromViewAssembler.toResource(post));
    }

    @DeleteMapping("/posts/{postKey}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal String currentUserKey, @PathVariable String postKey) {
        postCommandService.delete(currentUserKey, postKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postKey}/like")
    public PostResource like(@AuthenticationPrincipal String currentUserKey, @PathVariable String postKey) {
        return PostResourceFromViewAssembler.toResource(postCommandService.like(currentUserKey, postKey, true));
    }

    @DeleteMapping("/posts/{postKey}/like")
    public PostResource unlike(@AuthenticationPrincipal String currentUserKey, @PathVariable String postKey) {
        return PostResourceFromViewAssembler.toResource(postCommandService.like(currentUserKey, postKey, false));
    }

    @PostMapping("/posts/{postKey}/save")
    public PostResource save(@AuthenticationPrincipal String currentUserKey, @PathVariable String postKey) {
        return PostResourceFromViewAssembler.toResource(postCommandService.save(currentUserKey, postKey, true));
    }

    @DeleteMapping("/posts/{postKey}/save")
    public PostResource unsave(@AuthenticationPrincipal String currentUserKey, @PathVariable String postKey) {
        return PostResourceFromViewAssembler.toResource(postCommandService.save(currentUserKey, postKey, false));
    }

    @GetMapping("/me/saved-posts")
    public List<PostResource> saved(@AuthenticationPrincipal String currentUserKey) {
        return postQueryService.saved(currentUserKey).stream().map(PostResourceFromViewAssembler::toResource).toList();
    }

    @PostMapping("/posts/{postKey}/comments")
    public ResponseEntity<PostResource> comment(@AuthenticationPrincipal String currentUserKey,
                                                 @PathVariable String postKey,
                                                 @Valid @RequestBody CreateCommentResource resource) {
        var post = postCommandService.comment(currentUserKey, postKey, resource.text());
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResourceFromViewAssembler.toResource(post));
    }
}
