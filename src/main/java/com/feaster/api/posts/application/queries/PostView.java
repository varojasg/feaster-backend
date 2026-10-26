package com.feaster.api.posts.application.queries;

import com.feaster.api.posts.domain.model.aggregates.Post;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import java.time.Instant;
import java.util.List;

public record PostView(String postKey, String title, String description, String category,
                       List<String> hashtags, String mediaUrl, String mediaType,
                       AuthorPreview author, Instant createdAt, int likeCount,
                       int saveCount, List<Post.Comment> comments,
                       boolean likedByMe, boolean savedByMe) {}
