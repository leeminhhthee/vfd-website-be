package com.example.spring_vfdwebsite.services.partner;

import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerResponseDto;
import com.example.spring_vfdwebsite.dtos.partnerDTOs.PartnerUpdateRequestDto;

import java.util.List;

public interface PartnerService {

    PartnerResponseDto createPartner(PartnerCreateRequestDto dto);

    List<PartnerResponseDto> getAllPartners();

    PartnerResponseDto getPartnerById(Integer id);

    PartnerResponseDto updatePartner(PartnerUpdateRequestDto dto);

    void deletePartner(Integer id);
}
