package com.example.spring_vfdwebsite.services.affectedObject;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectResponseDto;
import com.example.spring_vfdwebsite.dtos.affectedObjectDTOs.AffectedObjectUpdateRequestDto;

public interface AffectedObjectService {
    AffectedObjectResponseDto createAffectedObject(AffectedObjectCreateRequestDto dto);

    AffectedObjectResponseDto updateAffectedObject(Integer id, AffectedObjectUpdateRequestDto dto);

    void deleteAffectedObject(Integer id);

    AffectedObjectResponseDto getAffectedObjectById(Integer id);

    List<AffectedObjectResponseDto> getAllAffectedObjects();
}
