package com.feaster.api.collections.application.commands;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;

public record CreateCollectionCommand(String name, String description, Visibility visibility) {}
