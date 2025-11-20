package com.example.spring_vfdwebsite.events.news;

import com.example.spring_vfdwebsite.entities.News;

import lombok.Value;

@Value
public class NewsCreatedEvent {
    private final Integer newsId;
    private final News createdNews;
}
