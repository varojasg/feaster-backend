package com.feaster.api.posts.domain.model.aggregates;

import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("posts")
public class Post {
    @Id
    public String postKey;
    public String title;
    public String description;
    public String category;
    public List<String> hashtags = new ArrayList<>();
    public String mediaUrl;
    public String mediaType;
    public AuthorPreview author;
    public Instant createdAt;
    public Set<String> likedByUserKeys = new HashSet<>();
    public Set<String> savedByUserKeys = new HashSet<>();
    public List<Comment> comments = new ArrayList<>();

    public record Comment(String commentKey, AuthorPreview author, String text, Instant createdAt) {
    }
}
