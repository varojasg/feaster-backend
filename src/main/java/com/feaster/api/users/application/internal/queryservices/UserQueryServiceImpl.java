package com.feaster.api.users.application.internal.queryservices;

import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.application.queries.UserView;
import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.domain.repositories.UserRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser(String userKey) {
        return userRepository.findById(userKey)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public UserView toView(User user) {
        int followingCount = user.followingUserKeys == null ? 0 : user.followingUserKeys.size();
        return new UserView(user.userKey, user.username, user.displayName, user.bio,
                followingCount, userRepository.countByFollowingUserKeysContaining(user.userKey));
    }

    public UserView profile(String userKey) {
        return toView(requireUser(userKey));
    }

    public List<UserView> list() {
        return userRepository.findAll().stream().map(this::toView)
                .sorted((first, second) -> first.username().compareTo(second.username()))
                .toList();
    }
}
