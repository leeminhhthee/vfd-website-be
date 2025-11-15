package com.example.spring_vfdwebsite.repositories;

import com.example.spring_vfdwebsite.entities.AffectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectedObjectJpaRepository extends JpaRepository<AffectedObject, Integer> {
    
}
