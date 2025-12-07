package com.example.spring_vfdwebsite.services.registrationForm;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.RegistrationFormCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.RegistrationFormRequestDto;
import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.RegistrationFormResponseDto;
import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.RegistrationFormUpdateRequestDto;
import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.TeamRegistrationDto;

public interface RegistrationFormService {
    RegistrationFormResponseDto createRegistrationForm(RegistrationFormCreateRequestDto dto);

    RegistrationFormResponseDto updateRegistrationForm(Integer id, RegistrationFormUpdateRequestDto dto);

    void deleteRegistrationForm(Integer id);

    RegistrationFormResponseDto getRegistrationFormById(Integer id);

    List<RegistrationFormResponseDto> getAllRegistrationForms();

    RegistrationFormResponseDto changeRegistrationFormStatus(Integer id, RegistrationFormRequestDto dto);

    List<TeamRegistrationDto> getRegistrationFormsByTournamentId(Integer tournamentId);
}
