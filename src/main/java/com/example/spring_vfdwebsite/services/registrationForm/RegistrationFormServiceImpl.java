package com.example.spring_vfdwebsite.services.registrationForm;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.*;
import com.example.spring_vfdwebsite.entities.RegistrationForm;
import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormCreatedEvent;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormDeletedEvent;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.RegistrationFormJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationFormServiceImpl implements RegistrationFormService {

    private final RegistrationFormJpaRepository registrationFormRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;

    // ===================== Get all =====================
    @Override
    @Cacheable(value = "registration-forms", key = "'all'")
    @Transactional(readOnly = true)
    public List<RegistrationFormResponseDto> getAllRegistrationForms() {
        return registrationFormRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "registration-forms", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public RegistrationFormResponseDto getRegistrationFormById(Integer id) {
        RegistrationForm form = registrationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RegistrationForm with id " + id + " not found"));
        return toDto(form);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = "registration-forms", allEntries = true)
    public RegistrationFormResponseDto createRegistrationForm(RegistrationFormCreateRequestDto dto) {
        RegistrationForm form = RegistrationForm.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .teamName(dto.getTeamName())
                .registrationUnit(dto.getRegistrationUnit())
                .numberAthletes(dto.getNumberAthletes())
                .fileUrl(dto.getFileUrl())
                .status(RegistrationStatusEnum.PENDING)
                .build();

        RegistrationForm savedForm = registrationFormRepository.save(form);

        eventPublisher.publishEvent(new RegistrationFormCreatedEvent(savedForm.getId(), savedForm));

        return toDto(savedForm);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "registration-forms", key = "#p0")
    @CacheEvict(value = "registration-forms", key = "'all'")
    public RegistrationFormResponseDto updateRegistrationForm(Integer id, RegistrationFormUpdateRequestDto dto) {
        RegistrationForm form = registrationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RegistrationForm with id " + id + " not found"));

        if (dto.getFullName() != null) {
            form.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null) {
            form.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            form.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getTeamName() != null) {
            form.setTeamName(dto.getTeamName());
        }
        if (dto.getRegistrationUnit() != null) {
            form.setRegistrationUnit(dto.getRegistrationUnit());
        }
        if (dto.getNumberAthletes() != null) {
            form.setNumberAthletes(dto.getNumberAthletes());
        }
        if (dto.getFileUrl() != null) {
            if (form.getFileUrl() != null) {
                cloudinaryUtils.deleteFile(form.getFileUrl());
            }
            form.setFileUrl(dto.getFileUrl());
        }
        if (dto.getStatus() != null) {
            form.setStatus(dto.getStatus());
        }

        RegistrationForm updatedForm = registrationFormRepository.save(form);

        eventPublisher.publishEvent(new RegistrationFormUpdatedEvent(updatedForm.getId(), updatedForm));

        return toDto(updatedForm);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "registration-forms", allEntries = true)
    public void deleteRegistrationForm(Integer id) {
        RegistrationForm form = registrationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration Form with id " + id + " not found"));

        if (form.getFileUrl() != null) {
            cloudinaryUtils.deleteFile(form.getFileUrl());
        }

        registrationFormRepository.delete(form);

        eventPublisher.publishEvent(new RegistrationFormDeletedEvent(id));
    }

    // =================== Mapping -> Dto ===================
    private RegistrationFormResponseDto toDto(RegistrationForm form) {
        return RegistrationFormResponseDto.builder()
                .id(form.getId())
                .fullName(form.getFullName())
                .email(form.getEmail())
                .phoneNumber(form.getPhoneNumber())
                .teamName(form.getTeamName())
                .registrationUnit(form.getRegistrationUnit())
                .numberAthletes(form.getNumberAthletes())
                .fileUrl(form.getFileUrl())
                .status(form.getStatus())
                .createdAt(form.getCreatedAt())
                .updatedAt(form.getUpdatedAt())
                .build();
    }

}
