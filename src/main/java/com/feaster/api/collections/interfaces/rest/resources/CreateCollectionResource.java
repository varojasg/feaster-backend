package com.feaster.api.collections.interfaces.rest.resources;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCollectionResource(@NotBlank @Size(max = 80) String name,
                                       @Size(max = 500) String description,
                                       @NotNull Visibility visibility) {}
