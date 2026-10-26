package com.feaster.api.auth.infrastructure.persistence.mongo.repositories;

import com.feaster.api.auth.domain.model.aggregates.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoSessionRepository extends MongoRepository<UserSession, String> {
}
