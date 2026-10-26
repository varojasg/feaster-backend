package com.feaster.api.auth.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record LoginResource(@NotBlank String username, @NotBlank String password) {}
