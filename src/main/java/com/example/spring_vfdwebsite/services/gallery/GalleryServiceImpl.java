package com.example.spring_vfdwebsite.services.gallery;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryUpdateRequestDto;
import com.example.spring_vfdwebsite.dtos.galleryDTOs.GalleryResponseDto;
import com.example.spring_vfdwebsite.entities.Gallery;
import com.example.spring_vfdwebsite.entities.Tournament;
import com.example.spring_vfdwebsite.events.gallery.GalleryCreatedEvent;
import com.example.spring_vfdwebsite.events.gallery.GalleryDeletedEvent;
import com.example.spring_vfdwebsite.events.gallery.GalleryUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.GalleryJpaRepository;
import com.example.spring_vfdwebsite.repositories.TournamentJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;
import com.example.spring_vfdwebsite.utils.SlugUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private final GalleryJpaRepository galleryRepository;
    private final TournamentJpaRepository tournamentRepository;
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
    @Cacheable(value = "galleries", key = "#root.args[0]")
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
    @LoggableAction(value = "CREATE", entity = "galleries", description = "Create new gallery")
    public GalleryResponseDto createGallery(GalleryCreateRequestDto dto) {

        Tournament tournament = null;
        if (dto.getTournament() != null) {
            tournament = tournamentRepository.findById(dto.getTournament())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Tournament not found with id " + dto.getTournament()));
        }

        String slug = generateUniqueSlug(dto.getTitle());

        Gallery gallery = Gallery.builder()
                .title(dto.getTitle())
                .slug(slug)
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .tournament(tournament)
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
    @CachePut(value = "galleries", key = "#p0")
    @LoggableAction(value = "UPDATE", entity = "galleries", description = "Update gallery")
    public GalleryResponseDto updateGallery(Integer id, GalleryUpdateRequestDto dto) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gallery not found with id " + id));

        boolean titleChanged = dto.getTitle() != null && !dto.getTitle().equals(gallery.getTitle());

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
        boolean slugMissing = gallery.getSlug() == null || gallery.getSlug().isBlank();
        if (slugMissing || titleChanged) {
            gallery.setSlug(generateUniqueSlug(gallery.getTitle()));
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
    @LoggableAction(value = "DELETE", entity = "galleries", description = "Delete gallery")
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

    // ===================== Get By Slug =====================
    @Override
    @Cacheable(value = "galleries", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public GalleryResponseDto getGalleryBySlug(String slug) {
        Gallery gallery = galleryRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Gallery not found with slug " + slug));
        return toDto(gallery);
    }

    // ===================== Mapper entity -> DTO =====================
    private GalleryResponseDto toDto(Gallery gallery) {
        GalleryResponseDto.TournamentDto tournamentDto = null;
        if (gallery.getTournament() != null) {
            tournamentDto = GalleryResponseDto.TournamentDto.builder()
                    .id(gallery.getTournament().getId())
                    .name(gallery.getTournament().getName())
                    .build();
        }

        return GalleryResponseDto.builder()
                .id(gallery.getId())
                .title(gallery.getTitle())
                .slug(gallery.getSlug())
                .category(gallery.getCategory())
                .imageUrl(gallery.getImageUrl())
                .tournament(tournamentDto)
                .createdAt(gallery.getCreatedAt())
                .updatedAt(gallery.getUpdatedAt())
                .build();
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (galleryRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
