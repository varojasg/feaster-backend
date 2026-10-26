package com.feaster.api.posts.domain.repositories;

import com.feaster.api.posts.domain.model.aggregates.Post;
import java.util.List;
import java.util.Optional;
public interface PostRepository {
    Optional<Post> findById(String postKey);
    List<Post> findAll();
    List<Post> findByAuthorKey(String userKey);
    List<Post> findSavedBy(String userKey);
    boolean existsById(String postKey);
    Post save(Post post);
    void delete(Post post);
}
