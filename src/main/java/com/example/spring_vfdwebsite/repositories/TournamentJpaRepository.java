package com.example.spring_vfdwebsite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.Tournament;

@Repository
public interface TournamentJpaRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Tournament> findByIdAndSlug(Integer id, String slug);
}
