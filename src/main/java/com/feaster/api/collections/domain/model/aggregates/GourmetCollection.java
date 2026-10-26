package com.feaster.api.collections.domain.model.aggregates;

import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("feast_collections")
public class GourmetCollection {
    @Id
    public String collectionKey;
    public String name;
    public String description;
    public AuthorPreview owner;
    public Visibility visibility;
    public Set<String> postKeys = new LinkedHashSet<>();
    public Instant createdAt;
    public Instant updatedAt;

    public enum Visibility { PUBLIC, PRIVATE }
}
