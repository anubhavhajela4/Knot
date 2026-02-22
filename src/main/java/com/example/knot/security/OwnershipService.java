package com.example.knot.security;


import com.example.knot.entity.User;
import com.example.knot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("ownershipService")
@RequiredArgsConstructor
public class OwnershipService {
    private final UserRepository userRepository;

    public boolean isOwner(UUID userId, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(principal instanceof UUID authId ) {
            return authId.equals(userId);
        }
        return false;
    }
}
