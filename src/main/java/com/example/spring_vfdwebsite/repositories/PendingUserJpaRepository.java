package com.example.spring_vfdwebsite.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.spring_vfdwebsite.entities.PendingUser;

public interface PendingUserJpaRepository extends JpaRepository<PendingUser, Integer> {
    Optional<PendingUser> findByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("DELETE FROM PendingUser p WHERE p.expiresAt < :now")
    void deleteExpiredPendingUsers(@Param("now") LocalDateTime now);
}
