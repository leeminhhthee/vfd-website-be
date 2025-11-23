package com.example.spring_vfdwebsite.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_vfdwebsite.entities.VerificationToken;

public interface OtpVerificationJpaRepository extends JpaRepository<VerificationToken, Integer> {
    
    Optional<VerificationToken> findByEmailAndOtpAndIsUsedFalse(String email, String otp);

    List<VerificationToken> findByEmailOrderByCreatedAtDesc(String email);

    Optional<VerificationToken> findFirstByEmailAndIsUsedFalseOrderByCreatedAtDesc(String email);

    void deleteByEmailAndExpiresAtBefore(String email, LocalDateTime dateTime);

    @Modifying
    @Query("DELETE FROM VerificationToken o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);
}
