package com.feaster.api.auth.interfaces.rest.transform;

import com.feaster.api.auth.application.commands.LoginCommand;
import com.feaster.api.auth.interfaces.rest.resources.LoginResource;

public final class LoginCommandFromResourceAssembler {
    private LoginCommandFromResourceAssembler() {}

    public static LoginCommand toCommand(LoginResource resource) {
        return new LoginCommand(resource.username(), resource.password());
    }
}
