package com.feaster.api.collections.interfaces.rest;

import com.feaster.api.collections.application.commandservices.CollectionCommandService;
import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.collections.interfaces.rest.resources.CollectionResource;
import com.feaster.api.collections.interfaces.rest.resources.CreateCollectionResource;
import com.feaster.api.collections.interfaces.rest.resources.UpdatePrivacyResource;
import com.feaster.api.collections.interfaces.rest.transform.CollectionResourceFromEntityAssembler;
import com.feaster.api.collections.interfaces.rest.transform.CreateCollectionCommandFromResourceAssembler;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CollectionController {
    private final CollectionCommandService collectionCommandService;
    private final CollectionQueryService collectionQueryService;

    public CollectionController(CollectionCommandService collectionCommandService,
                                CollectionQueryService collectionQueryService) {
        this.collectionCommandService = collectionCommandService;
        this.collectionQueryService = collectionQueryService;
    }

    @GetMapping("/collections/public")
    public List<CollectionResource> publicCollections() {
        return collectionQueryService.publicCollections().stream()
                .map(CollectionResourceFromEntityAssembler::toResource).toList();
    }

    @GetMapping("/collections/{collectionKey}")
    public CollectionResource find(@PathVariable String collectionKey,
                                   @AuthenticationPrincipal String currentUserKey) {
        return CollectionResourceFromEntityAssembler.toResource(
                collectionQueryService.visible(collectionKey, currentUserKey));
    }

    @GetMapping("/me/collections")
    public List<CollectionResource> mine(@AuthenticationPrincipal String currentUserKey) {
        return collectionQueryService.mine(currentUserKey).stream()
                .map(CollectionResourceFromEntityAssembler::toResource).toList();
    }

    @PostMapping("/collections")
    public ResponseEntity<CollectionResource> create(@AuthenticationPrincipal String currentUserKey,
                                                      @Valid @RequestBody CreateCollectionResource resource) {
        var collection = collectionCommandService.create(currentUserKey,
                CreateCollectionCommandFromResourceAssembler.toCommand(resource));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CollectionResourceFromEntityAssembler.toResource(collection));
    }

    @PatchMapping("/collections/{collectionKey}/privacy")
    public CollectionResource privacy(@AuthenticationPrincipal String currentUserKey,
                                       @PathVariable String collectionKey,
                                       @Valid @RequestBody UpdatePrivacyResource resource) {
        return CollectionResourceFromEntityAssembler.toResource(
                collectionCommandService.privacy(currentUserKey, collectionKey, resource.visibility()));
    }

    @DeleteMapping("/collections/{collectionKey}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal String currentUserKey,
                                        @PathVariable String collectionKey) {
        collectionCommandService.delete(currentUserKey, collectionKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/collections/{collectionKey}/posts/{postKey}")
    public CollectionResource addPost(@AuthenticationPrincipal String currentUserKey,
                                       @PathVariable String collectionKey, @PathVariable String postKey) {
        return CollectionResourceFromEntityAssembler.toResource(
                collectionCommandService.addPost(currentUserKey, collectionKey, postKey));
    }

    @DeleteMapping("/collections/{collectionKey}/posts/{postKey}")
    public CollectionResource removePost(@AuthenticationPrincipal String currentUserKey,
                                          @PathVariable String collectionKey, @PathVariable String postKey) {
        return CollectionResourceFromEntityAssembler.toResource(
                collectionCommandService.removePost(currentUserKey, collectionKey, postKey));
    }
}
