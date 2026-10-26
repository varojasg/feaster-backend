package com.feaster.api.collections.interfaces.rest.transform;

import com.feaster.api.collections.application.commands.CreateCollectionCommand;
import com.feaster.api.collections.interfaces.rest.resources.CreateCollectionResource;

public final class CreateCollectionCommandFromResourceAssembler {
    private CreateCollectionCommandFromResourceAssembler() {}

    public static CreateCollectionCommand toCommand(CreateCollectionResource resource) {
        return new CreateCollectionCommand(resource.name(), resource.description(), resource.visibility());
    }
}
