package com.example.spring_vfdwebsite.events.gallery;

import com.example.spring_vfdwebsite.entities.Gallery;

import lombok.Value;

@Value
public class GalleryUpdatedEvent {
    private final Integer galleryId;
    private final Gallery updatedGallery;
}
