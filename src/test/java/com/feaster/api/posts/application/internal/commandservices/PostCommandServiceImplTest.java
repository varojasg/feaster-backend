package com.feaster.api.posts.application.internal.commandservices;

import com.feaster.api.posts.application.queryservices.PostQueryService;
import com.feaster.api.posts.application.queries.PostView;
import com.feaster.api.posts.domain.model.aggregates.Post;
import com.feaster.api.posts.domain.model.valueobjects.AuthorPreview;
import com.feaster.api.posts.domain.repositories.PostRepository;
import com.feaster.api.shared.domain.exceptions.ApiException;
import com.feaster.api.users.domain.model.aggregates.User;
import com.feaster.api.users.application.queryservices.UserQueryService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceImplTest {
    @Mock PostRepository postRepository;
    @Mock UserQueryService userQueryService;
    @Mock MongoTemplate mongoTemplate;
    private PostCommandServiceImpl service;
    private Post post;

    @BeforeEach
    void prepare() {
        PostQueryService postQueryService = new com.feaster.api.posts.application.internal.queryservices.PostQueryServiceImpl(
                postRepository, userQueryService);
        service = new PostCommandServiceImpl(postRepository, userQueryService, postQueryService, mongoTemplate);
        post = new Post();
        post.postKey = "p1";
        post.author = new AuthorPreview("owner", "owner", "Owner");
        post.title = "Postre";
        post.createdAt = Instant.now();
    }

    @Test
    void repeatedLikeDoesNotDuplicate() {
        when(postRepository.findById("p1")).thenReturn(java.util.Optional.of(post));
        when(postRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        service.like("reader", "p1", true);
        var result = service.like("reader", "p1", true);
        assertEquals(1, result.likeCount());
        assertTrue(result.likedByMe());
    }

    @Test
    void unlikeRemovesReaction() {
        post.likedByUserKeys.add("reader");
        when(postRepository.findById("p1")).thenReturn(java.util.Optional.of(post));
        when(postRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        var result = service.like("reader", "p1", false);
        assertEquals(0, result.likeCount());
        assertFalse(result.likedByMe());
    }

    @Test
    void savingTwiceDoesNotDuplicate() {
        when(postRepository.findById("p1")).thenReturn(java.util.Optional.of(post));
        when(postRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        service.save("reader", "p1", true);
        var result = service.save("reader", "p1", true);
        assertEquals(1, result.saveCount());
        assertTrue(result.savedByMe());
    }

    @Test
    void anotherUserCannotDeletePost() {
        when(postRepository.findById("p1")).thenReturn(java.util.Optional.of(post));
        ApiException error = assertThrows(ApiException.class, () -> service.delete("reader", "p1"));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatus());
        verify(postRepository, never()).delete(any());
    }

    @Test
    void commentKeepsAuthorSnapshot() {
        User reader = new User();
        reader.userKey = "reader";
        reader.username = "reader";
        reader.displayName = "Reader";
        when(postRepository.findById("p1")).thenReturn(java.util.Optional.of(post));
        when(userQueryService.requireUser("reader")).thenReturn(reader);
        when(postRepository.save(any())).thenAnswer(call -> call.getArgument(0));
        var result = service.comment("reader", "p1", "Muy rico");
        assertEquals(1, result.comments().size());
        assertEquals("reader", result.comments().getFirst().author().userKey());
    }
}
