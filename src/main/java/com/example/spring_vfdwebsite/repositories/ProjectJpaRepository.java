package com.example.spring_vfdwebsite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.Project;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Project> findByIdAndSlug(Integer id, String slug);
}
