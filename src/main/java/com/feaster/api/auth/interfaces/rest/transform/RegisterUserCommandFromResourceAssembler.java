package com.feaster.api.auth.interfaces.rest.transform;

import com.feaster.api.auth.application.commands.RegisterUserCommand;
import com.feaster.api.auth.interfaces.rest.resources.RegisterResource;

public final class RegisterUserCommandFromResourceAssembler {
    private RegisterUserCommandFromResourceAssembler() {}

    public static RegisterUserCommand toCommand(RegisterResource resource) {
        return new RegisterUserCommand(resource.username(), resource.displayName(),
                resource.email(), resource.password());
    }
}
