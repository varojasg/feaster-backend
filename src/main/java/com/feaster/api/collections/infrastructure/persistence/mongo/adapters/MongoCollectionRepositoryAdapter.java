package com.feaster.api.collections.infrastructure.persistence.mongo.adapters;

import com.feaster.api.collections.domain.model.aggregates.GourmetCollection;
import com.feaster.api.collections.domain.repositories.CollectionRepository;
import com.feaster.api.collections.infrastructure.persistence.mongo.repositories.MongoCollectionRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoCollectionRepositoryAdapter implements CollectionRepository {
    private final MongoCollectionRepository repository;
    public MongoCollectionRepositoryAdapter(MongoCollectionRepository repository) { this.repository = repository; }
    public Optional<GourmetCollection> findById(String collectionKey) {
        return repository.findById(collectionKey);
    }
    public List<GourmetCollection> findByVisibility(GourmetCollection.Visibility visibility) {
        return repository.findByVisibility(visibility);
    }
    public List<GourmetCollection> findByOwnerKey(String userKey) {
        return repository.findByOwnerKey(userKey);
    }
    public List<GourmetCollection> findPublicByOwnerKey(String userKey) {
        return repository.findPublicByOwnerKey(userKey);
    }
    public GourmetCollection save(GourmetCollection collection) {
        return repository.save(collection);
    }
    public void delete(GourmetCollection collection) {
        repository.delete(collection);
    }
}
