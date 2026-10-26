package com.feaster.api.collections.application.internal.queryservices;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.collections.domain.repositories.CollectionRepository;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionQueryServiceImplTest {
    @Mock CollectionRepository collectionRepository;
    @Mock UserQueryService userQueryService;
    @InjectMocks CollectionQueryServiceImpl service;

    @Test
    void privateCollectionIsNotVisibleToAnotherUser() {
        GourmetCollection collection = new GourmetCollection();
        collection.collectionKey = "c1";
        collection.visibility = Visibility.PRIVATE;
        collection.owner = new AuthorPreview("owner", "owner", "Owner");
        when(collectionRepository.findById("c1")).thenReturn(Optional.of(collection));
        ApiException error = assertThrows(ApiException.class, () -> service.visible("c1", "reader"));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
    }

    @Test
    void privateCollectionIsVisibleToOwner() {
        GourmetCollection collection = new GourmetCollection();
        collection.collectionKey = "c1";
        collection.visibility = Visibility.PRIVATE;
        collection.owner = new AuthorPreview("owner", "owner", "Owner");
        when(collectionRepository.findById("c1")).thenReturn(Optional.of(collection));
        assertSame(collection, service.visible("c1", "owner"));
    }
}
