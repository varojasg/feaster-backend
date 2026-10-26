package com.feaster.api.auth.application.results;

import com.feaster.api.users.application.queries.UserView;

public record AuthResult(String token, UserView user) {}
