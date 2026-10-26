package com.feaster.api.users.application.commandservices;

import com.feaster.api.users.application.queries.UserView;

public interface UserCommandService {
    UserView follow(String currentUserKey, String otherUserKey);
    UserView unfollow(String currentUserKey, String otherUserKey);
}
