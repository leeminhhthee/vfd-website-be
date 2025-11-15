package com.example.spring_vfdwebsite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.spring_vfdwebsite.entities.Document;

@Repository
public interface DocumentJpaRepository extends JpaRepository<Document, Integer> {
    
}
