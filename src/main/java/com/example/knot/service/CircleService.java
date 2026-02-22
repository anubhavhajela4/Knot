package com.example.knot.service;

import com.example.knot.dto.CircleResponse;
import com.example.knot.dto.CreateCircleRequest;
import com.example.knot.entity.Circle;
import com.example.knot.entity.User;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.CircleRespository;
import com.example.knot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CircleService {

    private final CircleRespository circleRepository;
    private final UserRepository userRepository;

    public CircleService(CircleRespository circleRepository, UserRepository userRepository) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
    }

    public CircleResponse createCircle(CreateCircleRequest request, Authentication auth) {

        User creator = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Circle circle = Circle.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .createdBy(creator)
                .build();

        circle.getMembers().add(creator);
        circleRepository.save(circle);


    }


}
