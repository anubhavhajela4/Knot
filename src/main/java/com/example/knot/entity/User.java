package com.example.knot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;

    private String role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name="user_followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(mappedBy = "likedBy")
    private Set<Post> likedPost = new HashSet<>();

    @OneToMany(mappedBy =  "user" ,cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

}
