package com.example.spring_vfdwebsite.repositories;

import com.example.spring_vfdwebsite.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);

    Optional<User> findById(Integer id);

    Optional<User> findByEmailIgnoreCase(String email);
}
