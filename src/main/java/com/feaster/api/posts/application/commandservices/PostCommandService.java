package com.feaster.api.posts.application.commandservices;

import com.feaster.api.posts.application.commands.CreatePostCommand;
import com.feaster.api.posts.application.queries.PostView;

public interface PostCommandService {
    PostView create(String currentUserKey, CreatePostCommand command);
    void delete(String currentUserKey, String postKey);
    PostView like(String currentUserKey, String postKey, boolean enabled);
    PostView save(String currentUserKey, String postKey, boolean enabled);
    PostView comment(String currentUserKey, String postKey, String text);
}
