package com.feaster.api.collections.infrastructure.persistence.mongo.repositories;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoCollectionRepository extends MongoRepository<GourmetCollection, String> {
    List<GourmetCollection> findByVisibility(GourmetCollection.Visibility visibility);

    @Query("{ 'owner.userKey': ?0 }")
    List<GourmetCollection> findByOwnerKey(String userKey);

    @Query("{ 'owner.userKey': ?0, 'visibility': 'PUBLIC' }")
    List<GourmetCollection> findPublicByOwnerKey(String userKey);
}
