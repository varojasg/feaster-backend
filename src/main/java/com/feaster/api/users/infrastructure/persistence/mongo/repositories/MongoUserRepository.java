package com.feaster.api.users.infrastructure.persistence.mongo.repositories;

import com.feaster.api.users.domain.model.aggregates.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoUserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query(value = "{ 'followingUserKeys': ?0 }", count = true)
    long countByFollowingUserKeysContaining(String userKey);
}
