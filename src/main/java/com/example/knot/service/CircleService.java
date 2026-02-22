package com.example.knot.service;

import com.example.knot.dto.CircleResponse;
import com.example.knot.dto.CreateCircleRequest;
import com.example.knot.entity.Circle;
import com.example.knot.entity.User;
import com.example.knot.exception.*;
import com.example.knot.repository.CircleRespository;
import com.example.knot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CircleService {

    private final CircleRespository circleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CircleService(CircleRespository circleRepository,
                         UserRepository userRepository,
                         ModelMapper modelMapper) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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
        return modelMapper.map(circle, CircleResponse.class);

    }

    public void joinCircle(UUID circleId, Authentication auth) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(()->
                new CircleNotFoundException("Circle Not Found"));
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(()->
                new UserNotFoundException("User not found"));

        if(circle.getMembers().contains(user)) {
            throw new AlreadyMemberException("Already member of this circle");
        }
        circle.getMembers().add(user);
        circleRepository.save(circle);
    }

    public void leaveCircle(UUID circleId, Authentication auth) {

        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new ResourceNotFoundException("Circle not found"));

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!circle.getMembers().contains(user)) {
            throw new RuntimeException("You are not a member of this circle");
        }

        if (circle.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Creator cannot leave the circle");
        }

        circle.getMembers().remove(user);
        circleRepository.save(circle);
    }

    public CircleResponse getCircle(UUID id) {
        Circle circle = circleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Circle not found"));

        return modelMapper.map(circle, CircleResponse.class);
    }

    public List<CircleResponse> getMyCircles(Authentication auth) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return circleRepository.findByMembers_Id(user.getId())
                .stream()
                .map(circle1 -> modelMapper.map(circle1,CircleResponse.class))
                .toList();
    }


}
