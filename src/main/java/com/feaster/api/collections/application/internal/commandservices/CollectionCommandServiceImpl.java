package com.feaster.api.collections.application.internal.commandservices;

import com.feaster.api.collections.application.commandservices.CollectionCommandService;
import com.feaster.api.collections.application.commands.CreateCollectionCommand;
import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.collections.domain.repositories.CollectionRepository;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CollectionCommandServiceImpl implements CollectionCommandService {
    private final CollectionRepository collectionRepository;
    private final PostRepository postRepository;
    private final UserQueryService userQueryService;
    private final CollectionQueryService collectionQueryService;

    public CollectionCommandServiceImpl(CollectionRepository collectionRepository, PostRepository postRepository,
                                        UserQueryService userQueryService, CollectionQueryService collectionQueryService) {
        this.collectionRepository = collectionRepository;
        this.postRepository = postRepository;
        this.userQueryService = userQueryService;
        this.collectionQueryService = collectionQueryService;
    }

    public GourmetCollection create(String currentUserKey, CreateCollectionCommand command) {
        GourmetCollection collection = new GourmetCollection();
        collection.collectionKey = UUID.randomUUID().toString();
        collection.name = command.name().trim();
        collection.description = command.description() == null ? "" : command.description().trim();
        collection.owner = AuthorPreview.from(userQueryService.requireUser(currentUserKey));
        collection.visibility = command.visibility();
        collection.createdAt = Instant.now();
        collection.updatedAt = collection.createdAt;
        return collectionRepository.save(collection);
    }

    public GourmetCollection privacy(String currentUserKey, String collectionKey, Visibility visibility) {
        GourmetCollection collection = owned(currentUserKey, collectionKey);
        collection.visibility = visibility;
        collection.updatedAt = Instant.now();
        return collectionRepository.save(collection);
    }

    public void delete(String currentUserKey, String collectionKey) {
        collectionRepository.delete(owned(currentUserKey, collectionKey));
    }

    public GourmetCollection addPost(String currentUserKey, String collectionKey, String postKey) {
        GourmetCollection collection = owned(currentUserKey, collectionKey);
        if (!postRepository.existsById(postKey)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Publicación no encontrada");
        }
        collection.postKeys.add(postKey);
        collection.updatedAt = Instant.now();
        return collectionRepository.save(collection);
    }

    public GourmetCollection removePost(String currentUserKey, String collectionKey, String postKey) {
        GourmetCollection collection = owned(currentUserKey, collectionKey);
        collection.postKeys.remove(postKey);
        collection.updatedAt = Instant.now();
        return collectionRepository.save(collection);
    }

    private GourmetCollection owned(String currentUserKey, String collectionKey) {
        GourmetCollection collection = collectionQueryService.requireCollection(collectionKey);
        if (!Objects.equals(collection.owner.userKey(), currentUserKey)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Solo el propietario puede administrar esta colección");
        }
        return collection;
    }
}
