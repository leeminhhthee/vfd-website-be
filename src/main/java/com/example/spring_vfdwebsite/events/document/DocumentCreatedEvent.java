package com.example.spring_vfdwebsite.events.document;

import com.example.spring_vfdwebsite.entities.Document;

import lombok.Value;

@Value
public class DocumentCreatedEvent {
    private final Integer documentId;
    private final Document document;
}
