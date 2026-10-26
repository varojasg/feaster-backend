package com.feaster.api.users.infrastructure.persistence.mongo.adapters;

import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.domain.repositories.UserRepository;
import com.feaster.api.users.infrastructure.persistence.mongo.repositories.MongoUserRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoUserRepositoryAdapter implements UserRepository {
    private final MongoUserRepository repository;
    public MongoUserRepositoryAdapter(MongoUserRepository repository) { this.repository = repository; }
    public Optional<User> findById(String userKey) {
        return repository.findById(userKey);
    }
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public boolean existsById(String userKey) {
        return repository.existsById(userKey);
    }
    public List<User> findAll() {
        return repository.findAll();
    }
    public User save(User user) {
        return repository.save(user);
    }
    public long countByFollowingUserKeysContaining(String userKey) {
        return repository.countByFollowingUserKeysContaining(userKey);
    }
}
