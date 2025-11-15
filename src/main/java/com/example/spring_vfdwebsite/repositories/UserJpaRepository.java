package com.example.spring_vfdwebsite.repositories;

import com.example.spring_vfdwebsite.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);
}
