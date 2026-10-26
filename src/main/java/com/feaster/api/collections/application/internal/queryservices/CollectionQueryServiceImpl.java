package com.feaster.api.collections.application.internal.queryservices;

import com.feaster.api.collections.application.queryservices.CollectionQueryService;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.model.aggregates.GourmetCollection.Visibility;
import com.feaster.api.collections.domain.repositories.CollectionRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CollectionQueryServiceImpl implements CollectionQueryService {
    private final CollectionRepository collectionRepository;
    private final UserQueryService userQueryService;

    public CollectionQueryServiceImpl(CollectionRepository collectionRepository, UserQueryService userQueryService) {
        this.collectionRepository = collectionRepository;
        this.userQueryService = userQueryService;
    }

    public List<GourmetCollection> publicCollections() {
        return sorted(collectionRepository.findByVisibility(Visibility.PUBLIC));
    }

    public List<GourmetCollection> byOwner(String userKey) {
        userQueryService.requireUser(userKey);
        return sorted(collectionRepository.findPublicByOwnerKey(userKey));
    }

    public List<GourmetCollection> mine(String userKey) {
        return sorted(collectionRepository.findByOwnerKey(userKey));
    }

    public GourmetCollection visible(String collectionKey, String currentUserKey) {
        GourmetCollection collection = requireCollection(collectionKey);
        if (collection.visibility == Visibility.PRIVATE
                && !Objects.equals(collection.owner.userKey(), currentUserKey)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Colección no encontrada");
        }
        return collection;
    }

    public GourmetCollection requireCollection(String collectionKey) {
        return collectionRepository.findById(collectionKey)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Colección no encontrada"));
    }

    private List<GourmetCollection> sorted(List<GourmetCollection> collections) {
        return collections.stream().sorted(Comparator.comparing(
                (GourmetCollection collection) -> collection.createdAt).reversed()).toList();
    }
}
