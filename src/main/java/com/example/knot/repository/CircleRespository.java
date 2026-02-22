package com.example.knot.repository;

import com.example.knot.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CircleRespository extends JpaRepository<Circle, UUID> {
    Optional<Circle> findByName(String name);
}
