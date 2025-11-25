package com.example.spring_vfdwebsite.services.registrationForm;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.*;
import com.example.spring_vfdwebsite.entities.RegistrationForm;
import com.example.spring_vfdwebsite.entities.Tournament;
import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormCreatedEvent;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormDeletedEvent;
import com.example.spring_vfdwebsite.events.registrationForm.RegistrationFormUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.exceptions.HttpException;
import com.example.spring_vfdwebsite.repositories.RegistrationFormJpaRepository;
import com.example.spring_vfdwebsite.repositories.TournamentJpaRepository;
import com.example.spring_vfdwebsite.services.mailOtp.EmailService;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegistrationFormServiceImpl implements RegistrationFormService {

    private final RegistrationFormJpaRepository registrationFormRepository;
    private final TournamentJpaRepository tournamentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;
    private final EmailService emailService;

    // ===================== Get all =====================
    @Override
    @Cacheable(value = "registration-forms", key = "'all'")
    @Transactional(readOnly = true)
    public List<RegistrationFormResponseDto> getAllRegistrationForms() {
        System.out.println("🔥 Fetching all registration forms from the database...");
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
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tournament with id " + dto.getTournamentId() + " not found"));

        RegistrationForm form = RegistrationForm.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .teamName(dto.getTeamName())
                .registrationUnit(dto.getRegistrationUnit())
                .numberAthletes(dto.getNumberAthletes())
                .fileUrl(dto.getFileUrl())
                .status(RegistrationStatusEnum.PENDING)
                .tournament(tournament)
                .build();

        RegistrationForm savedForm = registrationFormRepository.save(form);

        emailService.sendRespondSubmitRegistrationFormEmail(form.getEmail(), form.getFullName(), form.getTeamName(),
                form.getPhoneNumber(), form.getRegistrationUnit(), form.getNumberAthletes(), form.getFileUrl(),
                form.getTournament().getName(), form.getStatus().name());

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

    // =================== Change Status ===================
    @Override
    @Transactional
    @CacheEvict(value = "registration-forms", key = "'all'")
    @CachePut(value = "registration-forms", key = "#p0")
    public RegistrationFormResponseDto changeRegistrationFormStatus(Integer id, RegistrationFormRequestDto dto) {
        RegistrationForm form = registrationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RegistrationForm with id " + id + " not found"));

        // 2. Kiểm tra status hợp lệ
        RegistrationStatusEnum newStatus = dto.getStatus();
        if (newStatus == null) {
            throw new HttpException("Status is required", HttpStatus.BAD_REQUEST);
        }

        form.setStatus(newStatus);
        form.setAdminNote(dto.getAdminNote());
        registrationFormRepository.save(form);

        log.info("RegistrationForm id={} status changed to {}", id, dto.getStatus());

        // 4. Gửi email phản hồi nếu status != pending
        if (newStatus != RegistrationStatusEnum.PENDING) {
            try {
                emailService.sendRegistrationApprovalResponseEmail(
                        form.getEmail(),
                        form.getFullName(),
                        form.getTeamName(),
                        form.getRegistrationUnit(),
                        form.getTournament().getName(),
                        newStatus.name(),
                        dto.getAdminNote());
            } catch (Exception e) {
                log.error("Failed to send approval email for registrationForm id={}", id, e);
            }
        }

        // if (!dto.getStatus().equalsIgnoreCase("pending")) {
        // String adminNote = ""; // Bạn có thể truyền note từ frontend nếu muốn
        // try {
        // emailService.sendRegistrationApprovalResponseEmail(
        // form.getEmail(),
        // form.getFullName(),
        // form.getTeamName(),
        // form.getRegistrationUnit(),
        // form.getTournament().getName(),
        // status,
        // adminNote
        // );
        // } catch (Exception e) {
        // log.error("Failed to send approval email for registrationForm id={}", id, e);
        // }
        // }

        eventPublisher.publishEvent(new RegistrationFormUpdatedEvent(form.getId(), form));

        return toDto(form);
    }

    // =================== Mapping -> Dto ===================
    private RegistrationFormResponseDto toDto(RegistrationForm form) {
        RegistrationFormResponseDto.TournamentDto tournamentDto = RegistrationFormResponseDto.TournamentDto.builder()
                .id(form.getTournament().getId())
                .name(form.getTournament().getName())
                .build();

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
                .tournament(tournamentDto)
                .adminNote(form.getAdminNote())
                .createdAt(form.getCreatedAt())
                .updatedAt(form.getUpdatedAt())
                .build();
    }

}
