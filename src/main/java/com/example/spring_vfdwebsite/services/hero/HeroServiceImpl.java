package com.example.spring_vfdwebsite.services.hero;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroResponseDto;
import com.example.spring_vfdwebsite.dtos.heroDTOs.HeroUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Hero;
import com.example.spring_vfdwebsite.events.hero.HeroCreatedEvent;
import com.example.spring_vfdwebsite.events.hero.HeroDeletedEvent;
import com.example.spring_vfdwebsite.events.hero.HeroUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.HeroJpaRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
@Transactional
public class HeroServiceImpl implements HeroService {

    private final HeroJpaRepository heroRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    // ===================== Get all =====================
    @Override
    @Cacheable(value = "heroes", key = "'all'")
    @Transactional(readOnly = true)
    public List<HeroResponseDto> getAllHeroes() {
        System.out.println("🔥 Fetching all heroes from the database...");
        return heroRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "heroes", key = "#id")
    // @Transactional(readOnly = true)
    public HeroResponseDto getHeroById(Integer id) {
        Hero hero = heroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hero with id " + id));
        return toDto(hero);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = "heroes", key = "'all'")
    public HeroResponseDto createHero(HeroCreateRequestDto dto) {
        Hero hero = Hero.builder()
                .title(dto.getTitle())
                .subTitle(dto.getSubTitle())
                .imageUrl(dto.getImageUrl())
                .buttonText(dto.getButtonText())
                .buttonHref(dto.getButtonHref())
                .buttonText2(dto.getButtonText2())
                .buttonHref2(dto.getButtonHref2())
                .build();

        hero = heroRepository.save(hero);

        eventPublisher.publishEvent(new HeroCreatedEvent(hero.getId(), hero));

        return toDto(hero);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "heroes", key = "#dto.id")
    @CacheEvict(value = "heroes", key = "'all'")
    public HeroResponseDto updateHero(HeroUpdateRequestDto dto) {
        Hero hero = heroRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Hero with id " + dto.getId()));

        if (dto.getTitle() != null) hero.setTitle(dto.getTitle());
        if (dto.getSubTitle() != null) hero.setSubTitle(dto.getSubTitle());
        if (dto.getImageUrl() != null) hero.setImageUrl(dto.getImageUrl());
        if (dto.getButtonText() != null) hero.setButtonText(dto.getButtonText());
        if (dto.getButtonHref() != null) hero.setButtonHref(dto.getButtonHref());
        if (dto.getButtonText2() != null) hero.setButtonText2(dto.getButtonText2());
        if (dto.getButtonHref2() != null) hero.setButtonHref2(dto.getButtonHref2());

        hero = heroRepository.save(hero);

        eventPublisher.publishEvent(new HeroUpdatedEvent(hero.getId(), hero));

        return toDto(hero);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "heroes", allEntries = true)
    public void deleteHero(Integer id) {
        Hero hero = heroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hero with id " + id));

        heroRepository.delete(hero);

        eventPublisher.publishEvent(new HeroDeletedEvent(id));
    }

    // ===================== Mapping =====================
    private HeroResponseDto toDto(Hero hero) {
        return HeroResponseDto.builder()
                .id(hero.getId())
                .title(hero.getTitle())
                .subTitle(hero.getSubTitle())
                .imageUrl(hero.getImageUrl())
                .buttonText(hero.getButtonText())
                .buttonHref(hero.getButtonHref())
                .buttonText2(hero.getButtonText2())
                .buttonHref2(hero.getButtonHref2())
                .createdAt(hero.getCreatedAt())
                .updatedAt(hero.getUpdatedAt())
                .build();
    }
}
