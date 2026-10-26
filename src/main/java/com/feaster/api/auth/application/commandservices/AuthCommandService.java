package com.feaster.api.auth.application.commandservices;

import com.feaster.api.auth.application.commands.LoginCommand;
import com.feaster.api.auth.application.commands.RegisterUserCommand;
import com.feaster.api.auth.application.results.AuthResult;
import java.util.Optional;

public interface AuthCommandService {
    AuthResult register(RegisterUserCommand command);
    AuthResult login(LoginCommand command);
    Optional<String> authenticate(String token);
    void logout(String authorization);
}
