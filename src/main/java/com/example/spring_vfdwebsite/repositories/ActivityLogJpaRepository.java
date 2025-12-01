package com.example.spring_vfdwebsite.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.entities.ActivityLog;

@Repository
public interface ActivityLogJpaRepository extends JpaRepository<ActivityLog, Integer> {
    @Query("SELECT al FROM ActivityLog al JOIN FETCH al.user ORDER BY al.createdAt DESC")
    List<ActivityLog> findAllWithUser();
    
    List<ActivityLog> findByUserId(Integer userId);
    List<ActivityLog> findByActionType(String actionType);
}
