package com.example.spring_vfdwebsite.services.gallery;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryResponseDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryUpdateRequestDto;

public interface GalleryService {

    GalleryResponseDto createGallery(GalleryCreateRequestDto dto);

    GalleryResponseDto updateGallery(Integer id, GalleryUpdateRequestDto dto);

    GalleryResponseDto getGalleryById(Integer id);

    List<GalleryResponseDto> getAllGalleries();

    void deleteGallery(Integer id);
    
}
