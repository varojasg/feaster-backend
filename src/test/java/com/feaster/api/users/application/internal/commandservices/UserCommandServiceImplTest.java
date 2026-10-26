package com.feaster.api.users.application.internal.commandservices;

import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.domain.repositories.UserRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock UserQueryService userQueryService;
    @InjectMocks UserCommandServiceImpl service;

    @Test
    void cannotFollowSelf() {
        ApiException error = assertThrows(ApiException.class, () -> service.follow("me", "me"));
        assertEquals(HttpStatus.CONFLICT, error.getStatus());
        verifyNoInteractions(userRepository, userQueryService);
    }

    @Test
    void repeatedFollowDoesNotDuplicate() {
        User current = new User();
        current.userKey = "me";
        User other = new User();
        other.userKey = "other";
        when(userQueryService.requireUser("other")).thenReturn(other);
        when(userQueryService.requireUser("me")).thenReturn(current);
        when(userRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        service.follow("me", "other");
        service.follow("me", "other");
        assertEquals(1, current.followingUserKeys.size());
        assertTrue(current.followingUserKeys.contains("other"));
    }
}
