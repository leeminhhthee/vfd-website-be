package com.example.spring_vfdwebsite.events.document;

import lombok.Value;

@Value
public class DocumentDeletedEvent {
    private final Integer documentId;
}
