package com.example.spring_vfdwebsite.services.gallery;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryUpdateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryResponseDto;
import com.example.spring_vfdwebsite.entities.Gallery;
import com.example.spring_vfdwebsite.events.gallery.GalleryCreatedEvent;
import com.example.spring_vfdwebsite.events.gallery.GalleryDeletedEvent;
import com.example.spring_vfdwebsite.events.gallery.GalleryUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.GalleryJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private final GalleryJpaRepository galleryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;

    // ===================== Get All =====================
    @Override
    @Cacheable(value = "galleries", key = "'all'")
    @Transactional(readOnly = true)
    public List<GalleryResponseDto> getAllGalleries() {
        System.out.println("🔥 Fetching all galleries from the database...");
        return galleryRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "galleries", key = "#id")
    @Transactional(readOnly = true)
    public GalleryResponseDto getGalleryById(Integer id) {
        return galleryRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Gallery not found with id " + id));
    }

    // ==================== Create ======================
    @Override
    @Transactional
    @CacheEvict(value = "galleries", allEntries = true)
    public GalleryResponseDto createGallery(GalleryCreateRequestDto dto) {

        Gallery gallery = Gallery.builder()
                .title(dto.getTitle())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .build();

        Gallery savedGallery = galleryRepository.save(gallery);

        // Publish event
        eventPublisher.publishEvent(new GalleryCreatedEvent(savedGallery.getId(), savedGallery));

        return toDto(savedGallery);
    }

    // ==================== Update ======================
    @Override
    @Transactional
    @CacheEvict(value = "galleries", allEntries = true)
    @CachePut(value = "galleries", key = "#dto.id")
    public GalleryResponseDto updateGallery(Integer id, GalleryUpdateRequestDto dto) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gallery not found with id " + id));

        if (dto.getTitle() != null) {
            gallery.setTitle(dto.getTitle());
        }
        if (dto.getCategory() != null) {
            gallery.setCategory(dto.getCategory());
        }
        if (dto.getImageUrl() != null) {
            List<String> oldImages = gallery.getImageUrl();
            List<String> newImages = dto.getImageUrl();

            if (oldImages != null && !oldImages.isEmpty()) {
                List<String> imagesToDelete = oldImages.stream()
                        .filter(img -> !newImages.contains(img))
                        .toList();
                cloudinaryUtils.deleteFiles(imagesToDelete);
            }

            // Cập nhật list ảnh mới
            gallery.setImageUrl(newImages);
        }

        Gallery updatedGallery = galleryRepository.save(gallery);

        // Publish event
        eventPublisher.publishEvent(new GalleryUpdatedEvent(updatedGallery.getId(), updatedGallery));

        return toDto(updatedGallery);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "galleries", allEntries = true)
    public void deleteGallery(Integer id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gallery not found with id " + id));

        if (gallery.getImageUrl() != null && !gallery.getImageUrl().isEmpty()) {
            cloudinaryUtils.deleteFiles(gallery.getImageUrl());
        }

        galleryRepository.delete(gallery);

        // Publish event
        eventPublisher.publishEvent(new GalleryDeletedEvent(id));
    }

    // ===================== Mapper entity -> DTO =====================
    private GalleryResponseDto toDto(Gallery gallery) {
        return GalleryResponseDto.builder()
                .id(gallery.getId())
                .title(gallery.getTitle())
                .category(gallery.getCategory())
                .imageUrl(gallery.getImageUrl())
                .createdAt(gallery.getCreatedAt())
                .updatedAt(gallery.getUpdatedAt())
                .build();
    }
}
