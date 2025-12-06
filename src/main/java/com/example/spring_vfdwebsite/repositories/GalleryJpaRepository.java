package com.example.spring_vfdwebsite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.spring_vfdwebsite.entities.Gallery;

@Repository
public interface GalleryJpaRepository extends JpaRepository<Gallery, Integer> {
    Optional<Gallery> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Gallery> findByIdAndSlug(Integer id, String slug);

    // Lấy tất cả gallery kèm tournament + imageUrl
    @Query("SELECT g FROM Gallery g LEFT JOIN FETCH g.tournament LEFT JOIN FETCH g.imageUrl")
    List<Gallery> findAllWithTournamentAndImages();

    // Lấy tất cả gallery kèm tournament
    @Query("SELECT DISTINCT g FROM Gallery g LEFT JOIN FETCH g.tournament")
    List<Gallery> findAllWithTournament();

    // Lấy 1 gallery theo ID kèm tournament + imageUrl
    @Query("SELECT g FROM Gallery g LEFT JOIN FETCH g.tournament LEFT JOIN FETCH g.imageUrl WHERE g.id = :id")
    Optional<Gallery> findByIdWithTournamentAndImages(@Param("id") Integer id);

}
