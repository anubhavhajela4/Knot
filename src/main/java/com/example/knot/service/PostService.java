package com.example.knot.service;

import com.example.knot.dto.CreatePostRequest;
import com.example.knot.dto.PostResponse;
import com.example.knot.entity.NotificationType;
import com.example.knot.entity.Post;
import com.example.knot.entity.User;
import com.example.knot.exception.PostAlreadyLikedException;
import com.example.knot.exception.PostNotFoundException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.NotificationRepository;
import com.example.knot.repository.PostRepository;
import com.example.knot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       ModelMapper modelMapper,
                       NotificationService notificationService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }

    public PostResponse createPost(UUID userId , CreatePostRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Post post = Post.builder()
                .content(request.getContent())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .timestamp(savedPost.getTimestamp())
                .userId(user.getId())
                .build();
    }

    public List<PostResponse> getPostByUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        return postRepository.findByUserId(userId)
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
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
        notificationService.createNotification(
                post.getUser(),
                user,
                NotificationType.LIKE,
                post.getId(),
                null
        );
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

    public List<PostResponse> getFeed(UUID userId,int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findFeedPosts(userId, pageable)
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
    }



}
