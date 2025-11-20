package com.example.spring_vfdwebsite.events.news;

import lombok.Value;

@Value
public class NewsDeletedEvent {
    private final Integer newsId;
}
