package com.feaster.api.users.domain.model.aggregates;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
    @Id
    public String userKey;
    @Indexed(unique = true)
    public String username;
    @Indexed(unique = true)
    public String email;
    public String displayName;
    public String bio;
    public String passwordHash;
    public Instant createdAt;
    public Set<String> followingUserKeys = new HashSet<>();
}
