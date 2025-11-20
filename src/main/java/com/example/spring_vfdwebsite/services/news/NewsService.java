package com.example.spring_vfdwebsite.services.news;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsResponseDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsUpdateRequestDto;

public interface NewsService {
    
    NewsResponseDto createNews(NewsCreateRequestDto dto);

    NewsResponseDto updateNews(Integer id, NewsUpdateRequestDto dto);

    void deleteNews(Integer id);

    NewsResponseDto getNewsById(Integer id);

    List<NewsResponseDto> getAllNews();
}
