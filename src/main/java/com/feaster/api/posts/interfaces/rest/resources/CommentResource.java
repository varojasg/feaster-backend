package com.feaster.api.posts.interfaces.rest.resources;

import com.feaster.api.shared.interfaces.rest.resources.AuthorResource;
import java.time.Instant;

public record CommentResource(String commentKey, AuthorResource author, String text, Instant createdAt) {}
