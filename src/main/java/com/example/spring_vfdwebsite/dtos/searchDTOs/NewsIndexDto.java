package com.example.spring_vfdwebsite.dtos.searchDTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.NewsTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsIndexDto {
    private String id;     
    private String title;  
    private String slug;  
    private NewsTypeEnum type; 
    private String content;  
    private String imageUrl;    
    private LocalDateTime createdAt;  
    private List<String> aiTags;  
}
