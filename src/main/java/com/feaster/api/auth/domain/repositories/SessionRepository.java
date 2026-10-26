package com.feaster.api.auth.domain.repositories;

import com.feaster.api.auth.domain.model.aggregates.UserSession;
import java.util.Optional;
public interface SessionRepository {
    Optional<UserSession> findById(String tokenHash);
    UserSession save(UserSession session);
    void deleteById(String tokenHash);
}
