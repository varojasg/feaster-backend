package com.feaster.api.posts.domain.model.valueobjects;

import com.feaster.api.users.domain.model.aggregates.User;

public record AuthorPreview(String userKey, String username, String displayName) {
    public static AuthorPreview from(User user) {
        return new AuthorPreview(user.userKey, user.username, user.displayName);
    }
}
