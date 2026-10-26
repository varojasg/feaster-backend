package com.feaster.api.collections.domain.repositories;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import java.util.List;
import java.util.Optional;
public interface CollectionRepository {
    Optional<GourmetCollection> findById(String collectionKey);
    List<GourmetCollection> findByVisibility(GourmetCollection.Visibility visibility);
    List<GourmetCollection> findByOwnerKey(String userKey);
    List<GourmetCollection> findPublicByOwnerKey(String userKey);
    GourmetCollection save(GourmetCollection collection);
    void delete(GourmetCollection collection);
}
