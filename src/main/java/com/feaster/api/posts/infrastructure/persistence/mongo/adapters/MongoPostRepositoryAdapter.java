package com.feaster.api.posts.infrastructure.persistence.mongo.adapters;

import com.feaster.api.posts.domain.model.aggregates.Post;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.posts.infrastructure.persistence.mongo.repositories.MongoPostRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoPostRepositoryAdapter implements PostRepository {
    private final MongoPostRepository repository;
    public MongoPostRepositoryAdapter(MongoPostRepository repository) { this.repository = repository; }
    public Optional<Post> findById(String postKey) {
        return repository.findById(postKey);
    }
    public List<Post> findAll() {
        return repository.findAll();
    }
    public List<Post> findByAuthorKey(String userKey) {
        return repository.findByAuthorKey(userKey);
    }
    public List<Post> findSavedBy(String userKey) {
        return repository.findSavedBy(userKey);
    }
    public boolean existsById(String postKey) {
        return repository.existsById(postKey);
    }
    public Post save(Post post) {
        return repository.save(post);
    }
    public void delete(Post post) {
        repository.delete(post);
    }
}
