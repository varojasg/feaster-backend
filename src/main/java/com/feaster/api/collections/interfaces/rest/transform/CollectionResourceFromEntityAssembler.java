package com.feaster.api.collections.interfaces.rest.transform;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.interfaces.rest.resources.CollectionResource;
import com.feaster.api.shared.interfaces.rest.resources.AuthorResource;

public final class CollectionResourceFromEntityAssembler {
    private CollectionResourceFromEntityAssembler() {}

    public static CollectionResource toResource(GourmetCollection collection) {
        return new CollectionResource(collection.collectionKey, collection.name,
                collection.description, AuthorResource.from(collection.owner),
                collection.visibility, collection.postKeys, collection.createdAt, collection.updatedAt);
    }
}
