package com.feaster.api.auth.interfaces.rest.resources;

import com.feaster.api.users.interfaces.rest.resources.UserResource;

public record AuthResource(String token, UserResource user) {}
