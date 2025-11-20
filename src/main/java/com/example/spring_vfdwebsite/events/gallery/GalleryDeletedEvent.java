package com.example.spring_vfdwebsite.events.gallery;

import lombok.Value;

@Value
public class GalleryDeletedEvent {
    private final Integer galleryId;
}
