package com.feaster.api.posts.infrastructure.persistence.mongo.repositories;

import com.feaster.api.posts.domain.model.aggregates.Post;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoPostRepository extends MongoRepository<Post, String> {
    @Query("{ 'author.userKey': ?0 }")
    List<Post> findByAuthorKey(String userKey);

    @Query("{ 'savedByUserKeys': ?0 }")
    List<Post> findSavedBy(String userKey);
}
