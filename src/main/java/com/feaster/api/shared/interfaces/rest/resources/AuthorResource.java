package com.feaster.api.shared.interfaces.rest.resources;

import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;

public record AuthorResource(String userKey, String username, String displayName) {
    public static AuthorResource from(AuthorPreview author) {
        return new AuthorResource(author.userKey(), author.username(), author.displayName());
    }
}
