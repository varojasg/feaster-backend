package com.feaster.api.auth.application.internal.commandservices;

import com.feaster.api.auth.application.commands.LoginCommand;
import com.feaster.api.auth.application.commands.RegisterUserCommand;
import com.feaster.api.auth.domain.model.aggregates.UserSession;
import com.feaster.api.auth.domain.repositories.SessionRepository;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.users.application.queries.UserView;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock SessionRepository sessionRepository;
    @Mock UserQueryService userQueryService;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks AuthCommandServiceImpl service;

    @Test
    void registerRejectsDuplicatedUsername() {
        when(userRepository.findByUsername("luna")).thenReturn(Optional.of(new User()));
        ApiException error = assertThrows(ApiException.class,
                () -> service.register(new RegisterUserCommand("luna", "Luna", "luna@example.org", "password123")));
        assertEquals(HttpStatus.CONFLICT, error.getStatus());
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginRejectsWrongPassword() {
        User user = new User();
        user.passwordHash = "encoded";
        when(userRepository.findByUsername("luna")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        ApiException error = assertThrows(ApiException.class,
                () -> service.login(new LoginCommand("luna", "wrong")));
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
        verifyNoInteractions(sessionRepository);
    }

    @Test
    void registerReturnsTokenAndSafeProfile() {
        when(passwordEncoder.encode("password123")).thenReturn("hash");
        when(userQueryService.toView(any())).thenReturn(new UserView("id", "luna", "Luna", "", 0, 0));
        var result = service.register(new RegisterUserCommand("Luna", "Luna", "Luna@Example.org", "password123"));
        assertFalse(result.token().isBlank());
        assertEquals("luna", result.user().username());
        verify(userRepository).save(argThat(user -> user.username.equals("luna")
                && user.email.equals("luna@example.org") && user.passwordHash.equals("hash")));
        verify(sessionRepository).save(argThat(session -> session.tokenHash.length() == 64
                && !session.tokenHash.equals(result.token())));
    }
}
