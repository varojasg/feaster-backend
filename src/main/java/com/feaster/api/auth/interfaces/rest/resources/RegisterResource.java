package com.feaster.api.auth.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterResource(@NotBlank String username, @NotBlank String displayName,
                               @Email @NotBlank String email, @NotBlank @Size(min = 8) String password) {}
