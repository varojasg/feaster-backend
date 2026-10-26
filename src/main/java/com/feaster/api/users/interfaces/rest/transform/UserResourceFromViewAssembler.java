package com.feaster.api.users.interfaces.rest.transform;

import com.feaster.api.users.application.queries.UserView;
import com.feaster.api.users.interfaces.rest.resources.UserResource;

public final class UserResourceFromViewAssembler {
    private UserResourceFromViewAssembler() {}

    public static UserResource toResource(UserView user) {
        return new UserResource(user.userKey(), user.username(), user.displayName(), user.bio(),
                user.followingCount(), user.followerCount());
    }
}
