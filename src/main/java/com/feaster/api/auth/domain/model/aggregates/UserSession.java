package com.feaster.api.auth.domain.model.aggregates;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sessions")
public class UserSession {
    @Id
    public String tokenHash;
    public String userKey;
    @Indexed(expireAfter = "0s")
    public Instant expiresAt;
}
