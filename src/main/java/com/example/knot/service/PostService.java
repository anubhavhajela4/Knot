package com.example.knot.service;

import com.example.knot.dto.CreatePostRequest;
import com.example.knot.dto.PostResponse;
import com.example.knot.entity.Post;
import com.example.knot.entity.User;
import com.example.knot.exception.PostAlreadyLikedException;
import com.example.knot.exception.PostNotFoundException;
import com.example.knot.exception.UserNotFoundException;
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

    public void likePost(UUID postId,UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post Not Found"));
        if(post.getLikedBy().contains(user)) {
            throw new PostAlreadyLikedException("Post Already Liked");
        }
        post.getLikedBy().add(user);
        postRepository.save(post);
    }

    public void unlikePost(UUID postId,UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post NotFound"));
        if(!post.getLikedBy().contains(user)) {
            throw new RuntimeException("Post Not Liked");
        }
        post.getLikedBy().remove(user);
        postRepository.save(post);
    }

    public int getLikeCount(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post Not Found"));
        return post.getLikedBy().size();
    }

    public List<PostResponse> getFeed(UUID userId) {

        return postRepository.findFeedPosts(userId)
                .stream()
                .map(post -> PostResponse.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .createdAt(post.getTimestamp())
                        .userId(post.getUser().getId())
                        .build())
                .toList();
    }



}
