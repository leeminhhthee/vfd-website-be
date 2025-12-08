package com.example.spring_vfdwebsite.services.project;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectResponseDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Bank;
import com.example.spring_vfdwebsite.entities.Project;
import com.example.spring_vfdwebsite.events.project.ProjectCreatedEvent;
import com.example.spring_vfdwebsite.events.project.ProjectDeletedEvent;
import com.example.spring_vfdwebsite.events.project.ProjectUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.BankJpaRepository;
import com.example.spring_vfdwebsite.repositories.ProjectJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;
import com.example.spring_vfdwebsite.utils.SlugUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final CloudinaryUtils cloudinaryUtils;
    private final ProjectJpaRepository projectRepository;
    private final BankJpaRepository bankRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===================== Get All =====================
    @Override
    @Cacheable(value = {"projects", "banks"}, key = "'all'")
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {
        System.out.println("🔥 Fetching all projects from the database...");
        return projectRepository.findAllProject()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = {"projects", "banks"}, key = "#root.args[0]")
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Integer id) {
        Project project = projectRepository.findByIdProject(id)
                .orElseThrow(() -> new IllegalArgumentException("Project with id " + id + " not found"));
        return toDto(project);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = {"projects", "banks"}, allEntries = true)
    @LoggableAction(value = "CREATE", entity = "projects", description = "Create a new project")
    public ProjectResponseDto createProject(ProjectCreateRequestDto dto) {

        Bank bank = bankRepository.findById(dto.getBankId())
                .orElseThrow(() -> new EntityNotFoundException("Bank with id " + dto.getBankId() + " not found"));

        String slug = generateUniqueSlug(dto.getTitle());

        Project project = Project.builder()
                .title(dto.getTitle())
                .slug(slug)
                .overview(dto.getOverview())
                .duration(dto.getDuration())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .goals(dto.getGoals())
                .bank(bank)
                .build();
        project = projectRepository.save(project);

        eventPublisher.publishEvent(new ProjectCreatedEvent(project.getId(), project));

        return toDto(project);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = {"projects", "banks"}, key = "#p0")
    @CacheEvict(value = { "projects", "banks" }, allEntries = true)
    @LoggableAction(value = "UPDATE", entity = "projects", description = "Update an existing project")
    public ProjectResponseDto updateProject(Integer id, ProjectUpdateRequestDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " not found"));

        boolean titleChanged = dto.getTitle() != null && !dto.getTitle().equals(project.getTitle());

        if (dto.getTitle() != null)
            project.setTitle(dto.getTitle());
        if (dto.getOverview() != null)
            project.setOverview(dto.getOverview());
        if (dto.getDuration() != null)
            project.setDuration(dto.getDuration());
        if (dto.getLocation() != null)
            project.setLocation(dto.getLocation());
        if (dto.getPrice() != null)
            project.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null)
            project.setImageUrl(dto.getImageUrl());
        if (dto.getCategory() != null)
            project.setCategory(dto.getCategory());
        if (dto.getGoals() != null)
            project.setGoals(dto.getGoals());
        if (dto.getBankId() != null) {
            Bank bank = bankRepository.findById(dto.getBankId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank with id " + dto.getBankId() + " not found"));
            project.setBank(bank);
        }
        boolean slugMissing = project.getSlug() == null || project.getSlug().isBlank();
        if (slugMissing || titleChanged) {
            project.setSlug(generateUniqueSlug(project.getTitle()));
        }

        project = projectRepository.save(project);

        eventPublisher.publishEvent(new ProjectUpdatedEvent(project.getId(), project));

        return toDto(project);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = { "projects", "banks" }, allEntries = true)
    @LoggableAction(value = "DELETE", entity = "projects", description = "Delete an existing project")
    public void deleteProject(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " not found"));

        if (project.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(project.getImageUrl());
        }

        projectRepository.deleteById(id);
        eventPublisher.publishEvent(new ProjectDeletedEvent(id));
    }

    // ===================== Get By Slug =====================
    @Override
    @Cacheable(value = {"projects", "banks"}, key = "#root.args[0]")
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Project with slug " + slug + " not found"));
        return toDto(project);
    }

    // ===================== Get By Id and Slug =====================
    @Override
    @Cacheable(value = {"projects", "banks"}, key = "#root.args[0] + '-' + #root.args[1]")
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectByIdSlug(Integer id, String slug) {
        Project project = projectRepository.findByIdAndSlug(id, slug)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " and slug " + slug + " not found"));
        return toDto(project);
    }

    // ===================== Mapper entity -> DTO =====================
    private ProjectResponseDto toDto(Project project) {
        ProjectResponseDto.BankDto bankDto = null;
        if (project.getBank() != null) {
            bankDto = ProjectResponseDto.BankDto.builder()
                    .id(project.getBank().getId())
                    .fullName(project.getBank().getFullName())
                    .bankName(project.getBank().getBankName())
                    .accountNumber(project.getBank().getAccountNumber())
                    .branch(project.getBank().getBranch())
                    .imageUrl(project.getBank().getImageUrl())
                    .build();
        }
        return ProjectResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .overview(project.getOverview())
                .duration(project.getDuration())
                .location(project.getLocation())
                .price(project.getPrice())
                .imageUrl(project.getImageUrl())
                .category(project.getCategory())
                .goals(project.getGoals())
                .bank(bankDto)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (projectRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
