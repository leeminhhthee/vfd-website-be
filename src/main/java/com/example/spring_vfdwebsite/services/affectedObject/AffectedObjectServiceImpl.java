package com.example.spring_vfdwebsite.services.affectedObject;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectResponseDto;
import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.AffectedObject;
import com.example.spring_vfdwebsite.events.affectedObjects.AffectedObjectCreatedEvent;
import com.example.spring_vfdwebsite.events.affectedObjects.AffectedObjectDeteledEvent;
import com.example.spring_vfdwebsite.events.affectedObjects.AffectedObjectUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.AffectedObjectJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AffectedObjectServiceImpl implements AffectedObjectService {

    private final CloudinaryUtils cloudinaryUtils;
    private final AffectedObjectJpaRepository affectedObjectRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Create
    @Override
    @CacheEvict(value = "affected-objects", key = "'all'")
    public AffectedObjectResponseDto createAffectedObject(
            AffectedObjectCreateRequestDto affectedObjectCreateRequestDto) {
        AffectedObject obj = AffectedObject.builder()
                .title(affectedObjectCreateRequestDto.getTitle())
                .description(affectedObjectCreateRequestDto.getDescription())
                .imageUrl(affectedObjectCreateRequestDto.getImageUrl())
                .build();

        obj = affectedObjectRepository.save(obj);

        // Phát sự kiện AffectedObjectCreatedEvent
        eventPublisher.publishEvent(new AffectedObjectCreatedEvent(obj.getId(), obj));
        return toDto(obj);
    }

    // Update
    @Override
    @CachePut(value = "affected-objects", key = "#p0")
    @CacheEvict(value = "affected-objects", key = "'all'")
    public AffectedObjectResponseDto updateAffectedObject(Integer id, 
            AffectedObjectUpdateRequestDto affectedObjectUpdateRequestDto) {
        AffectedObject obj = affectedObjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "AffectedObject with id " + id + " not found"));

        if (affectedObjectUpdateRequestDto.getTitle() != null)
            obj.setTitle(affectedObjectUpdateRequestDto.getTitle());
        if (affectedObjectUpdateRequestDto.getDescription() != null)
            obj.setDescription(affectedObjectUpdateRequestDto.getDescription());
        if (affectedObjectUpdateRequestDto.getImageUrl() != null)
            obj.setImageUrl(affectedObjectUpdateRequestDto.getImageUrl());
        obj = affectedObjectRepository.save(obj);

        // Phát sự kiện AffectedObjectUpdatedEvent
        eventPublisher.publishEvent(new AffectedObjectUpdatedEvent(obj.getId(), obj));

        return toDto(obj);
    }

    // Delete
    @Override
    @CacheEvict(value = "affected-objects", allEntries = true)
    public void deleteAffectedObject(Integer id) {
        AffectedObject obj = affectedObjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AffectedObject with id " + id + " not found"));

        if (obj.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(obj.getImageUrl());
        }
        affectedObjectRepository.delete(obj);

        // Phát sự kiện AffectedObjectDeletedEvent
        eventPublisher.publishEvent(new AffectedObjectDeteledEvent(id));
    }

    // Get by ID
    @Override
    @Cacheable(value = "affected-objects", key = "#root.args[0]")
    public AffectedObjectResponseDto getAffectedObjectById(Integer id) {
        System.out.println("Cache key = " + id);
        return affectedObjectRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("AffectedObject with id " + id + " not found"));
    }

    // Get all
    @Override
    @Cacheable(value = "affected-objects", key = "'all'")
    public List<AffectedObjectResponseDto> getAllAffectedObjects() {
        System.out.println("🔥 Fetching all affected objects from the database...");
        return affectedObjectRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Mapper entity -> DTO
    private AffectedObjectResponseDto toDto(AffectedObject obj) {
        return AffectedObjectResponseDto.builder()
                .id(obj.getId())
                .title(obj.getTitle())
                .description(obj.getDescription())
                .imageUrl(obj.getImageUrl())
                .createdAt(obj.getCreatedAt())
                .updatedAt(obj.getUpdatedAt())
                .build();
    }

}
