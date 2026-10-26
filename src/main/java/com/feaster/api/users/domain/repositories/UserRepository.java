package com.feaster.api.users.domain.repositories;

import com.feaster.api.users.domain.model.aggregates.User;
import java.util.List;
import java.util.Optional;
public interface UserRepository {
    Optional<User> findById(String userKey);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsById(String userKey);
    List<User> findAll();
    User save(User user);
    long countByFollowingUserKeysContaining(String userKey);
}
