package com.feaster.api.collections.interfaces.rest.resources;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.shared.interfaces.rest.resources.AuthorResource;
import java.time.Instant;
import java.util.Set;

public record CollectionResource(String collectionKey, String name, String description,
                                 AuthorResource owner, Visibility visibility, Set<String> postKeys,
                                 Instant createdAt, Instant updatedAt) {}
