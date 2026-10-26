package com.feaster.api.posts.application.internal.queryservices;

import com.feaster.api.posts.application.queryservices.PostQueryService;
import com.feaster.api.posts.application.queries.PostView;
import com.feaster.api.posts.domain.model.aggregates.Post;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;
    private final UserQueryService userQueryService;

    public PostQueryServiceImpl(PostRepository postRepository, UserQueryService userQueryService) {
        this.postRepository = postRepository;
        this.userQueryService = userQueryService;
    }

    public Post requirePost(String postKey) {
        return postRepository.findById(postKey)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Publicación no encontrada"));
    }

    public List<PostView> list(String query, String currentUserKey) {
        String searchText = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        return postRepository.findAll().stream()
                .filter(post -> searchText.isEmpty() || contains(post.title, searchText)
                        || contains(post.description, searchText) || contains(post.category, searchText)
                        || post.hashtags.stream().anyMatch(tag -> contains(tag, searchText)))
                .sorted(Comparator.comparing((Post post) -> post.createdAt).reversed())
                .map(post -> toView(post, currentUserKey)).toList();
    }

    private boolean contains(String field, String searchText) {
        return field != null && field.toLowerCase(Locale.ROOT).contains(searchText);
    }

    public PostView find(String postKey, String currentUserKey) {
        return toView(requirePost(postKey), currentUserKey);
    }

    public List<PostView> byAuthor(String userKey) {
        userQueryService.requireUser(userKey);
        return postRepository.findByAuthorKey(userKey).stream()
                .sorted(Comparator.comparing((Post post) -> post.createdAt).reversed())
                .map(post -> toView(post, null)).toList();
    }

    public List<PostView> saved(String currentUserKey) {
        return postRepository.findSavedBy(currentUserKey).stream()
                .sorted(Comparator.comparing((Post post) -> post.createdAt).reversed())
                .map(post -> toView(post, currentUserKey)).toList();
    }

    public PostView toView(Post post, String currentUserKey) {
        boolean liked = currentUserKey != null && post.likedByUserKeys.contains(currentUserKey);
        boolean saved = currentUserKey != null && post.savedByUserKeys.contains(currentUserKey);
        return new PostView(post.postKey, post.title, post.description, post.category,
                post.hashtags, post.mediaUrl, post.mediaType, post.author, post.createdAt,
                post.likedByUserKeys.size(), post.savedByUserKeys.size(), post.comments, liked, saved);
    }
}
