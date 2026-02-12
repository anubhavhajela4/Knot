package com.example.knot.repository;

import com.example.knot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(UUID userId);

    @Query("""
    SELECT p FROM Post p
    WHERE p.user.id IN (
    SELECT u.id FROM User me
    JOIN me.following u
    WHERE me.id = :userId
    )
    ORDER BY p.timestamp DESC
""")
    List<Post> findFeedPosts(UUID userId);
}
