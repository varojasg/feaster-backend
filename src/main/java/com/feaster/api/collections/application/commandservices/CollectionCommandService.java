package com.feaster.api.collections.application.commandservices;

import com.feaster.api.collections.application.commands.CreateCollectionCommand;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;

public interface CollectionCommandService {
    GourmetCollection create(String currentUserKey, CreateCollectionCommand command);
    GourmetCollection privacy(String currentUserKey, String collectionKey, Visibility visibility);
    void delete(String currentUserKey, String collectionKey);
    GourmetCollection addPost(String currentUserKey, String collectionKey, String postKey);
    GourmetCollection removePost(String currentUserKey, String collectionKey, String postKey);
}
