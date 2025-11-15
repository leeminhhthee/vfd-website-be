package com.example.spring_vfdwebsite.repositories;

import com.example.spring_vfdwebsite.entities.BoardDirector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardDirectorJpaRepository extends JpaRepository<BoardDirector, Integer> {
    List<BoardDirector> findByEmail(String email);
}
