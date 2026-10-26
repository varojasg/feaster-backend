package com.feaster.api.posts.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentResource(@NotBlank @Size(max = 500) String text) {}
