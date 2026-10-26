package com.feaster.api.auth.interfaces.rest;

import com.feaster.api.auth.application.commandservices.AuthCommandService;
import com.feaster.api.auth.interfaces.rest.resources.AuthResource;
import com.feaster.api.auth.interfaces.rest.resources.RegisterResource;
import com.feaster.api.auth.interfaces.rest.resources.LoginResource;
import com.feaster.api.auth.interfaces.rest.transform.AuthResourceFromResultAssembler;
import com.feaster.api.auth.interfaces.rest.transform.RegisterUserCommandFromResourceAssembler;
import com.feaster.api.auth.interfaces.rest.transform.LoginCommandFromResourceAssembler;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.interfaces.rest.resources.UserResource;
import com.feaster.api.users.interfaces.rest.transform.UserResourceFromViewAssembler;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthCommandService authCommandService;
    private final UserQueryService userQueryService;

    public AuthController(AuthCommandService authCommandService, UserQueryService userQueryService) {
        this.authCommandService = authCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResource> register(@Valid @RequestBody RegisterResource resource) {
        var result = authCommandService.register(RegisterUserCommandFromResourceAssembler.toCommand(resource));
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResourceFromResultAssembler.toResource(result));
    }

    @PostMapping("/login")
    public AuthResource login(@Valid @RequestBody LoginResource resource) {
        return AuthResourceFromResultAssembler.toResource(
                authCommandService.login(LoginCommandFromResourceAssembler.toCommand(resource)));
    }

    @GetMapping("/me")
    public Map<String, UserResource> me(@AuthenticationPrincipal String currentUserKey) {
        return Map.of("user", UserResourceFromViewAssembler.toResource(userQueryService.profile(currentUserKey)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        authCommandService.logout(authorization);
        return ResponseEntity.noContent().build();
    }
}
