package com.example.spring_vfdwebsite.repositories;

import com.example.spring_vfdwebsite.entities.RefreshToken;
import com.example.spring_vfdwebsite.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByUserId(Integer userId);
}
