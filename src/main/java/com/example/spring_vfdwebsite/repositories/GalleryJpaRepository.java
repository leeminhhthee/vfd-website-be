package com.example.spring_vfdwebsite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.spring_vfdwebsite.entities.Gallery;

@Repository
public interface GalleryJpaRepository extends JpaRepository<Gallery, Integer> {
    Optional<Gallery> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
