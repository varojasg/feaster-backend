package com.feaster.api.auth.interfaces.rest.transform;

import com.feaster.api.auth.application.results.AuthResult;
import com.feaster.api.auth.interfaces.rest.resources.AuthResource;
import com.feaster.api.users.interfaces.rest.transform.UserResourceFromViewAssembler;

public final class AuthResourceFromResultAssembler {
    private AuthResourceFromResultAssembler() {}

    public static AuthResource toResource(AuthResult result) {
        return new AuthResource(result.token(), UserResourceFromViewAssembler.toResource(result.user()));
    }
}
