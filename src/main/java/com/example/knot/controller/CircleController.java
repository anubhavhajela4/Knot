package com.example.knot.controller;

import com.example.knot.dto.CircleResponse;
import com.example.knot.dto.CreateCircleRequest;
import com.example.knot.service.CircleService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/circles")
public class CircleController {

    private final CircleService circleService;

    public CircleController(CircleService circleService) {
        this.circleService = circleService;
    }
    @PostMapping
    public CircleResponse createCircle(@Valid @RequestBody CreateCircleRequest request,
                                       Authentication auth) {
        return circleService.createCircle(request,auth);
    }

    @PostMapping("/{id}/join")
    public void joinCircle(@PathVariable UUID id, Authentication authentication) {
        circleService.joinCircle(id, authentication);
    }

    @PostMapping("/{id}/leave")
    public void leaveCircle(@PathVariable UUID id, Authentication authentication) {
        circleService.leaveCircle(id, authentication);
    }

    @GetMapping("/{id}")
    public CircleResponse getCircle(@PathVariable UUID id) {
        return circleService.getCircle(id);
    }

    @GetMapping("/me")
    public List<CircleResponse> getMyCircles(Authentication authentication) {
        return circleService.getMyCircles(authentication);
    }
}
