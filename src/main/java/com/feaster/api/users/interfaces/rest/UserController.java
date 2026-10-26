package com.feaster.api.users.interfaces.rest;

import com.feaster.api.users.application.commandservices.UserCommandService;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.posts.application.queryservices.PostQueryService;
import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.users.interfaces.rest.resources.UserResource;
import com.feaster.api.users.interfaces.rest.resources.FoodieContentResource;
import com.feaster.api.users.interfaces.rest.transform.UserResourceFromViewAssembler;
import com.feaster.api.posts.interfaces.rest.transform.PostResourceFromViewAssembler;
import com.feaster.api.posts.interfaces.rest.resources.PostResource;
import com.feaster.api.collections.interfaces.rest.transform.CollectionResourceFromEntityAssembler;
import com.feaster.api.collections.interfaces.rest.resources.CollectionResource;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final PostQueryService postQueryService;
    private final CollectionQueryService collectionQueryService;

    public UserController(UserQueryService userQueryService, UserCommandService userCommandService,
                          PostQueryService postQueryService, CollectionQueryService collectionQueryService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.postQueryService = postQueryService;
        this.collectionQueryService = collectionQueryService;
    }

    @GetMapping("/users")
    public List<UserResource> list() {
        return userQueryService.list().stream().map(UserResourceFromViewAssembler::toResource).toList();
    }

    @GetMapping("/users/{userKey}")
    public UserResource profile(@PathVariable String userKey) {
        return UserResourceFromViewAssembler.toResource(userQueryService.profile(userKey));
    }

    @GetMapping("/users/{userKey}/posts")
    public List<PostResource> posts(@PathVariable String userKey) {
        return postQueryService.byAuthor(userKey).stream()
                .map(PostResourceFromViewAssembler::toResource).toList();
    }

    @GetMapping("/users/{userKey}/collections")
    public List<CollectionResource> collections(
            @PathVariable String userKey) {
        return collectionQueryService.byOwner(userKey).stream()
                .map(CollectionResourceFromEntityAssembler::toResource).toList();
    }

    @GetMapping("/foodies/{foodieKey}/public-content")
    public FoodieContentResource publicContent(@PathVariable String foodieKey) {
        return new FoodieContentResource(
                UserResourceFromViewAssembler.toResource(userQueryService.profile(foodieKey)),
                posts(foodieKey), collections(foodieKey));
    }

    @PostMapping("/users/{userKey}/follow")
    public UserResource follow(@AuthenticationPrincipal String currentUserKey, @PathVariable String userKey) {
        return UserResourceFromViewAssembler.toResource(userCommandService.follow(currentUserKey, userKey));
    }

    @DeleteMapping("/users/{userKey}/follow")
    public UserResource unfollow(@AuthenticationPrincipal String currentUserKey, @PathVariable String userKey) {
        return UserResourceFromViewAssembler.toResource(userCommandService.unfollow(currentUserKey, userKey));
    }
}
