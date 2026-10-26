package com.feaster.api.posts.application.internal.commandservices;

import com.feaster.api.posts.application.commandservices.PostCommandService;
import com.feaster.api.posts.application.commands.CreatePostCommand;
import com.feaster.api.posts.application.queryservices.PostQueryService;
import com.feaster.api.posts.application.queries.PostView;
import com.feaster.api.posts.domain.model.aggregates.Post;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final UserQueryService userQueryService;
    private final PostQueryService postQueryService;
    private final MongoTemplate mongoTemplate;

    public PostCommandServiceImpl(PostRepository postRepository, UserQueryService userQueryService,
                                  PostQueryService postQueryService, MongoTemplate mongoTemplate) {
        this.postRepository = postRepository;
        this.userQueryService = userQueryService;
        this.postQueryService = postQueryService;
        this.mongoTemplate = mongoTemplate;
    }

    public PostView create(String currentUserKey, CreatePostCommand command) {
        if (!command.mediaUrl().matches("(?i)^https?://[^\\s]+$")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "mediaUrl debe ser una URL http o https");
        }
        Post post = new Post();
        post.postKey = UUID.randomUUID().toString();
        post.title = command.title().trim();
        post.description = command.description().trim();
        post.category = command.category().trim();
        post.hashtags = command.hashtags() == null ? List.of() : command.hashtags().stream()
                .filter(Objects::nonNull).map(String::trim).filter(tag -> !tag.isBlank())
                .distinct().toList();
        post.mediaUrl = command.mediaUrl();
        post.mediaType = command.mediaType().toUpperCase(Locale.ROOT);
        post.author = AuthorPreview.from(userQueryService.requireUser(currentUserKey));
        post.createdAt = Instant.now();
        return postQueryService.toView(postRepository.save(post), currentUserKey);
    }

    public void delete(String currentUserKey, String postKey) {
        Post post = postQueryService.requirePost(postKey);
        if (!Objects.equals(post.author.userKey(), currentUserKey)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Solo el autor puede eliminar esta publicación");
        }
        postRepository.delete(post);
        // La colección guarda referencias; deben retirarse al borrar la publicación.
        mongoTemplate.updateMulti(new Query(Criteria.where("postKeys").is(postKey)),
                new Update().pull("postKeys", postKey), "feast_collections");
    }

    public PostView like(String currentUserKey, String postKey, boolean enabled) {
        Post post = postQueryService.requirePost(postKey);
        if (enabled) post.likedByUserKeys.add(currentUserKey);
        else post.likedByUserKeys.remove(currentUserKey);
        return postQueryService.toView(postRepository.save(post), currentUserKey);
    }

    public PostView save(String currentUserKey, String postKey, boolean enabled) {
        Post post = postQueryService.requirePost(postKey);
        if (enabled) post.savedByUserKeys.add(currentUserKey);
        else post.savedByUserKeys.remove(currentUserKey);
        return postQueryService.toView(postRepository.save(post), currentUserKey);
    }

    public PostView comment(String currentUserKey, String postKey, String text) {
        Post post = postQueryService.requirePost(postKey);
        post.comments.add(new Post.Comment(UUID.randomUUID().toString(),
                AuthorPreview.from(userQueryService.requireUser(currentUserKey)), text.trim(), Instant.now()));
        return postQueryService.toView(postRepository.save(post), currentUserKey);
    }
}
