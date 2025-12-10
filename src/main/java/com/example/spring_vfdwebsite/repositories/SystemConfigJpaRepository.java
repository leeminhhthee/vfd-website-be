package com.example.spring_vfdwebsite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.SystemConfig;

@Repository
public interface SystemConfigJpaRepository extends JpaRepository<SystemConfig, Integer> {
    Optional<SystemConfig> findByKey(String key);
}
