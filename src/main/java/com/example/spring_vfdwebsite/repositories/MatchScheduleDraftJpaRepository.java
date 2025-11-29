package com.example.spring_vfdwebsite.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.MatchScheduleDraft;

@Repository
public interface MatchScheduleDraftJpaRepository extends JpaRepository<MatchScheduleDraft, Integer> {
    List<MatchScheduleDraft> findByTournament_Id(Integer tournamentId);
}
