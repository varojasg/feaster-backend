package com.feaster.api.auth.infrastructure.persistence.mongo.adapters;

import com.feaster.api.auth.domain.model.aggregates.UserSession;
import com.feaster.api.auth.domain.repositories.SessionRepository;
import com.feaster.api.auth.infrastructure.persistence.mongo.repositories.MongoSessionRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class MongoSessionRepositoryAdapter implements SessionRepository {
    private final MongoSessionRepository repository;
    public MongoSessionRepositoryAdapter(MongoSessionRepository repository) { this.repository = repository; }
    public Optional<UserSession> findById(String tokenHash) {
        return repository.findById(tokenHash);
    }
    public UserSession save(UserSession session) {
        return repository.save(session);
    }
    public void deleteById(String tokenHash) {
        repository.deleteById(tokenHash);
    }
}
