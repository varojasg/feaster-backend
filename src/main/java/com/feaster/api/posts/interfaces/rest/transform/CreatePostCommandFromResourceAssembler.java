package com.feaster.api.posts.interfaces.rest.transform;

import com.feaster.api.posts.application.commands.CreatePostCommand;
import com.feaster.api.posts.interfaces.rest.resources.CreatePostResource;

public final class CreatePostCommandFromResourceAssembler {
    private CreatePostCommandFromResourceAssembler() {}

    public static CreatePostCommand toCommand(CreatePostResource resource) {
        return new CreatePostCommand(resource.title(), resource.description(), resource.category(),
                resource.hashtags(), resource.mediaUrl(), resource.mediaType());
    }
}
