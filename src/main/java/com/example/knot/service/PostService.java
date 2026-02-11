package com.example.knot.service;

import com.example.knot.dto.CreatePostRequest;
import com.example.knot.dto.PostResponse;
import com.example.knot.entity.Post;
import com.example.knot.entity.User;
import com.example.knot.repository.PostRepository;
import com.example.knot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostResponse createPost(UUID userId , CreatePostRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        Post post = Post.builder()
                .content(request.getContent())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(savedPost.getTimestamp())
                .userId(user.getId())
                .build();
    }

    public List<PostResponse> getPostByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        return postRepository.findByUserId(userId)
                .stream()
                .map(post -> PostResponse.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .createdAt(post.getTimestamp())
                        .userId(user.getId())
                        .build())
                .toList();

    }


}
