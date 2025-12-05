package com.example.spring_vfdwebsite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.News;

@Repository
public interface NewsJpaRepository extends JpaRepository<News, Integer> {
    Optional<News> findBySlug(String slug);

    boolean existsBySlug(String slug);

}
