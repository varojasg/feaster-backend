package com.feaster.api.posts.interfaces.rest.transform;

import com.feaster.api.posts.application.queries.PostView;
import com.feaster.api.posts.interfaces.rest.resources.CommentResource;
import com.feaster.api.posts.interfaces.rest.resources.PostResource;
import com.feaster.api.shared.interfaces.rest.resources.AuthorResource;

public final class PostResourceFromViewAssembler {
    private PostResourceFromViewAssembler() {}

    public static PostResource toResource(PostView post) {
        var comments = post.comments().stream()
                .map(comment -> new CommentResource(comment.commentKey(),
                        AuthorResource.from(comment.author()), comment.text(), comment.createdAt()))
                .toList();
        return new PostResource(post.postKey(), post.title(), post.description(), post.category(),
                post.hashtags(), post.mediaUrl(), post.mediaType(), AuthorResource.from(post.author()),
                post.createdAt(), post.likeCount(), post.saveCount(), comments,
                post.likedByMe(), post.savedByMe());
    }
}
