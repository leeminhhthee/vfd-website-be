package com.example.spring_vfdwebsite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.News;

@Repository
public interface NewsJpaRepository extends JpaRepository<News, Integer> {
    Optional<News> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<News> findByIdAndSlug(Integer id, String slug);

    // Query methods get all news
    @Query("SELECT n FROM News n LEFT JOIN FETCH n.authorBy")
    List<News> findAllNews();

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.authorBy WHERE n.id = :id")
    Optional<News> findByIdNews(@Param("id") Integer id);

}
