package com.feaster.api.posts.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePostResource(@NotBlank @Size(max = 120) String title,
                                 @NotBlank @Size(max = 2000) String description,
                                 @NotBlank String category, List<String> hashtags,
                                 @NotBlank String mediaUrl,
                                 @NotBlank @Pattern(regexp = "IMAGE|VIDEO") String mediaType) {}
