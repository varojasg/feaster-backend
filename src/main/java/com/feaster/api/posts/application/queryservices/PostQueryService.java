package com.feaster.api.posts.application.queryservices;

import com.feaster.api.posts.application.queries.PostView;
import com.feaster.api.posts.domain.model.aggregates.Post;
import java.util.List;

public interface PostQueryService {
    Post requirePost(String postKey);
    List<PostView> list(String query, String currentUserKey);
    PostView find(String postKey, String currentUserKey);
    List<PostView> byAuthor(String userKey);
    List<PostView> saved(String currentUserKey);
    PostView toView(Post post, String currentUserKey);
}
