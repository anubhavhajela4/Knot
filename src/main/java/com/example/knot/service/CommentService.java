package com.example.knot.service;


import com.example.knot.dto.CommentResponse;
import com.example.knot.dto.CreateCommentRequest;
import com.example.knot.entity.Comment;
import com.example.knot.entity.Post;
import com.example.knot.entity.User;
import com.example.knot.exception.PostNotFoundException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.CommentRepository;
import com.example.knot.repository.PostRepository;
import com.example.knot.repository.UserRepository;
import org.hibernate.annotations.Comments;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse createComment(UUID userId, UUID postId, CreateCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .createdAt(savedComment.getCreatedAt())
                .userId(user.getId())
                .postId(post.getId())
                .build();

    }

    public List<CommentResponse> getComments(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return post.getComments()
                .stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .userId(comment.getUser().getId())
                        .postId(post.getId())
                        .build())
                .toList();
    }

}
