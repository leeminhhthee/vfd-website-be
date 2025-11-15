package com.example.spring_vfdwebsite.services.partner;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.cache.annotation.*;

import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerResponseDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Partner;
import com.example.spring_vfdwebsite.events.partner.PartnerCreatedEvent;
import com.example.spring_vfdwebsite.events.partner.PartnerDeletedEvent;
import com.example.spring_vfdwebsite.events.partner.PartnerUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.PartnerJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerServiceImpl implements PartnerService {

    private final PartnerJpaRepository partnerRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    // ===== Helper mapping =====
    private PartnerResponseDto toDto(Partner partner) {
        return PartnerResponseDto.builder()
                .id(partner.getId())
                .name(partner.getName())
                .email(partner.getEmail())
                .imageUrl(partner.getImageUrl())
                .since(partner.getSince())
                .createdAt(partner.getCreatedAt())
                .updatedAt(partner.getUpdatedAt())
                .build();
    }

    // ===== Create Partner =====
    @Override
    @Transactional
    @CacheEvict(value = "partners", key = "'all'")
    public PartnerResponseDto createPartner(PartnerCreateRequestDto createRequestDto) {
        Partner partner = Partner.builder()
                .name(createRequestDto.getName())
                .email(createRequestDto.getEmail())
                .imageUrl(createRequestDto.getImageUrl())
                .since(createRequestDto.getSince())
                .build();

        partner = partnerRepository.save(partner);

        eventPublisher.publishEvent(new PartnerCreatedEvent(partner.getId(), partner));

        return toDto(partner);
    }

    // ===== Get all partners =====
    @Override
    @Cacheable(value = "partners", key = "'all'")
    public List<PartnerResponseDto> getAllPartners() {
        System.out.println("🔥 Fetching all partner from the database...");
        return partnerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    // ===== Get partner by ID =====
    @Override
    @Cacheable(value = "partners", key = "#id")
    public PartnerResponseDto getPartnerById(Integer id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partner with id " + id));
        return toDto(partner);
    }

    // ===== Update partner =====
    @Override
    @Transactional
    @CachePut(value = "partners", key = "#updateRequestDto.id")
    @CacheEvict(value = "partners", key = "'all'")
    public PartnerResponseDto updatePartner(PartnerUpdateRequestDto updateRequestDto) {
        Partner partner = partnerRepository.findById(updateRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Partner with id " + updateRequestDto.getId()));

        if (updateRequestDto.getName() != null) partner.setName(updateRequestDto.getName());
        if (updateRequestDto.getEmail() != null) partner.setEmail(updateRequestDto.getEmail());
        if (updateRequestDto.getImageUrl() != null) partner.setImageUrl(updateRequestDto.getImageUrl());
        if (updateRequestDto.getSince() != null) partner.setSince(updateRequestDto.getSince());

        partner = partnerRepository.save(partner);

        eventPublisher.publishEvent(new PartnerUpdatedEvent(partner.getId(), partner));

        return toDto(partner);
    }

    // ===== Delete partner =====
    @Override
    @Transactional
    @CacheEvict(value = "partners", allEntries = true)
    public void deletePartner(Integer id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partner with id " + id));

        partnerRepository.delete(partner);

        eventPublisher.publishEvent(new PartnerDeletedEvent(id));
    }
}
