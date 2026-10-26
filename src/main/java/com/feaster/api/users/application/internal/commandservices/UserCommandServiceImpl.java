package com.feaster.api.users.application.internal.commandservices;

import com.feaster.api.users.application.commandservices.UserCommandService;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.application.queries.UserView;
import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.domain.repositories.UserRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public UserCommandServiceImpl(UserRepository userRepository, UserQueryService userQueryService) {
        this.userRepository = userRepository;
        this.userQueryService = userQueryService;
    }

    public UserView follow(String currentUserKey, String otherUserKey) {
        if (Objects.equals(currentUserKey, otherUserKey)) {
            throw new ApiException(HttpStatus.CONFLICT, "No puedes seguirte a ti mismo");
        }
        userQueryService.requireUser(otherUserKey);
        User currentUser = userQueryService.requireUser(currentUserKey);
        currentUser.followingUserKeys.add(otherUserKey);
        return userQueryService.toView(userRepository.save(currentUser));
    }

    public UserView unfollow(String currentUserKey, String otherUserKey) {
        userQueryService.requireUser(otherUserKey);
        User currentUser = userQueryService.requireUser(currentUserKey);
        currentUser.followingUserKeys.remove(otherUserKey);
        return userQueryService.toView(userRepository.save(currentUser));
    }
}
