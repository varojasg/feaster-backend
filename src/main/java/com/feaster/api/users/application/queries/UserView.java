package com.feaster.api.users.application.queries;

public record UserView(String userKey, String username, String displayName, String bio,
                       int followingCount, long followerCount) {
}
