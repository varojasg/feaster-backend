package com.feaster.api.users.interfaces.rest.resources;

public record UserResource(String userKey, String username, String displayName, String bio,
                           int followingCount, long followerCount) {}
