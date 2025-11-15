package com.example.spring_vfdwebsite.services.hero;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.heroDTOs.*;

public interface HeroService {
    List<HeroResponseDto> getAllHeroes();

    HeroResponseDto getHeroById(Integer id);

    HeroResponseDto createHero(HeroCreateRequestDto dto);

    HeroResponseDto updateHero(HeroUpdateRequestDto dto);

    void deleteHero(Integer id);
}
