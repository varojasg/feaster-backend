package com.feaster.api.posts.interfaces.rest.resources;

import com.feaster.api.shared.interfaces.rest.resources.AuthorResource;
import java.time.Instant;
import java.util.List;

public record PostResource(String postKey, String title, String description, String category,
                           List<String> hashtags, String mediaUrl, String mediaType,
                           AuthorResource author, Instant createdAt, int likeCount,
                           int saveCount, List<CommentResource> comments,
                           boolean likedByMe, boolean savedByMe) {}
