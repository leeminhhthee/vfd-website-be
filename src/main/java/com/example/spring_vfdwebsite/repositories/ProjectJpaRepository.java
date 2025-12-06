package com.example.spring_vfdwebsite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.Project;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Project> findByIdAndSlug(Integer id, String slug);

    @Query("""
                SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.bank ORDER BY p.createdAt DESC
            """)
    List<Project> findAllProject();

    @Query("""
                SELECT p FROM Project p LEFT JOIN FETCH p.bank WHERE p.id = :id
            """)
    Optional<Project> findByIdProject(@Param("id") Integer id);
}
