package com.feaster.api.collections.interfaces.rest.resources;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import jakarta.validation.constraints.NotNull;

public record UpdatePrivacyResource(@NotNull Visibility visibility) {}
