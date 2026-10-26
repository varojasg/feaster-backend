package com.feaster.api.collections.application.internal.commandservices;

import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.collections.domain.repositories.CollectionRepository;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionCommandServiceImplTest {
    @Mock CollectionRepository collectionRepository;
    @Mock PostRepository postRepository;
    @Mock UserQueryService userQueryService;
    @Mock CollectionQueryService collectionQueryService;
    private CollectionCommandServiceImpl service;
    private GourmetCollection collection;

    @BeforeEach
    void prepare() {
        service = new CollectionCommandServiceImpl(collectionRepository, postRepository,
                userQueryService, collectionQueryService);
        collection = new GourmetCollection();
        collection.collectionKey = "c1";
        collection.owner = new AuthorPreview("owner", "owner", "Owner");
        collection.visibility = Visibility.PRIVATE;
        collection.createdAt = Instant.now();
    }

    @Test
    void anotherUserCannotChangePrivacy() {
        when(collectionQueryService.requireCollection("c1")).thenReturn(collection);
        ApiException error = assertThrows(ApiException.class,
                () -> service.privacy("reader", "c1", Visibility.PUBLIC));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatus());
        verify(collectionRepository, never()).save(any());
    }

    @Test
    void ownerCanChangePrivacy() {
        when(collectionQueryService.requireCollection("c1")).thenReturn(collection);
        when(collectionRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        var result = service.privacy("owner", "c1", Visibility.PUBLIC);
        assertEquals(Visibility.PUBLIC, result.visibility);
        verify(collectionRepository).save(collection);
    }

    @Test
    void ownerCannotAddMissingPost() {
        when(collectionQueryService.requireCollection("c1")).thenReturn(collection);
        ApiException error = assertThrows(ApiException.class,
                () -> service.addPost("owner", "c1", "missing"));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        verify(collectionRepository, never()).save(any());
    }

    @Test
    void addingPostTwiceDoesNotDuplicateReference() {
        when(collectionQueryService.requireCollection("c1")).thenReturn(collection);
        when(postRepository.existsById("p1")).thenReturn(true);
        when(collectionRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        service.addPost("owner", "c1", "p1");
        service.addPost("owner", "c1", "p1");
        assertEquals(1, collection.postKeys.size());
    }
}
