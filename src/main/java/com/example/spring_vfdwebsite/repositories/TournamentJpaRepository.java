package com.example.spring_vfdwebsite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.Tournament;

@Repository
public interface TournamentJpaRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Tournament> findByIdAndSlug(Integer id, String slug);

    // Query methods get all tournaments with relations
    @Query("""
        SELECT DISTINCT t FROM Tournament t
        LEFT JOIN FETCH t.createdBy
        LEFT JOIN FETCH t.tournamentDocuments
        LEFT JOIN FETCH t.matchSchedules
    """)
    List<Tournament> findAllTournament();

    // Query method to get tournament by id with relations
    @Query("""
        SELECT t FROM Tournament t
        LEFT JOIN FETCH t.createdBy
        LEFT JOIN FETCH t.tournamentDocuments
        LEFT JOIN FETCH t.matchSchedules
        WHERE t.id = :id
    """)
    Optional<Tournament> findByIdTournament(@Param("id") Integer id);
}
