package com.feaster.api.auth.application.internal.commandservices;

import com.feaster.api.auth.domain.model.aggregates.UserSession;
import com.feaster.api.auth.domain.repositories.SessionRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.domain.repositories.UserRepository;
import com.feaster.api.users.application.queryservices.UserQueryService;
import com.feaster.api.auth.application.commandservices.AuthCommandService;
import com.feaster.api.auth.application.commands.LoginCommand;
import com.feaster.api.auth.application.commands.RegisterUserCommand;
import com.feaster.api.auth.application.results.AuthResult;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthCommandServiceImpl implements AuthCommandService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthCommandServiceImpl(UserRepository userRepository, SessionRepository sessionRepository,
                                  UserQueryService userQueryService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.userQueryService = userQueryService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResult register(RegisterUserCommand command) {
        String username = command.username();
        String displayName = command.displayName();
        String email = command.email();
        String password = command.password();
        String cleanUsername = username.trim().toLowerCase(java.util.Locale.ROOT);
        String cleanEmail = email.trim().toLowerCase(java.util.Locale.ROOT);
        if (!cleanUsername.matches("[a-z0-9_]{3,30}")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El usuario debe tener de 3 a 30 letras, números o guiones bajos");
        }
        if (userRepository.findByUsername(cleanUsername).isPresent() || userRepository.findByEmail(cleanEmail).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "El usuario o correo ya está registrado");
        }
        User user = new User();
        user.userKey = UUID.randomUUID().toString();
        user.username = cleanUsername;
        user.displayName = displayName.trim();
        user.email = cleanEmail;
        user.bio = "";
        user.passwordHash = passwordEncoder.encode(password);
        user.createdAt = Instant.now();
        try {
            userRepository.save(user);
        } catch (DuplicateKeyException exception) {
            throw new ApiException(HttpStatus.CONFLICT, "El usuario o correo ya está registrado");
        }
        return createSession(user);
    }

    public AuthResult login(LoginCommand command) {
        String username = command.username();
        String password = command.password();
        User user = userRepository.findByUsername(username.trim().toLowerCase(java.util.Locale.ROOT))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));
        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }
        return createSession(user);
    }

    public Optional<String> authenticate(String token) {
        return sessionRepository.findById(hash(token))
                .filter(session -> session.expiresAt.isAfter(Instant.now()))
                .filter(session -> userRepository.existsById(session.userKey))
                .map(session -> session.userKey);
    }

    public void logout(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            sessionRepository.deleteById(hash(authorization.substring(7).trim()));
        }
    }

    private AuthResult createSession(User user) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        UserSession session = new UserSession();
        session.tokenHash = hash(token);
        session.userKey = user.userKey;
        session.expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        sessionRepository.save(session);
        return new AuthResult(token, userQueryService.toView(user));
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no disponible", exception);
        }
    }

}
