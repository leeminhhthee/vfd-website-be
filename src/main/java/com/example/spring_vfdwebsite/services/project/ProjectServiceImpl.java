package com.example.spring_vfdwebsite.services.project;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectResponseDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Project;
import com.example.spring_vfdwebsite.events.project.ProjectCreatedEvent;
import com.example.spring_vfdwebsite.events.project.ProjectDeletedEvent;
import com.example.spring_vfdwebsite.events.project.ProjectUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.ProjectJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final CloudinaryUtils cloudinaryUtils;
    private final ProjectJpaRepository projectRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===================== Get All =====================
    @Override
    @Cacheable(value = "projects", key = "'all'")
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {
        System.out.println("🔥 Fetching all projects from the database...");
        return projectRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "projects", key = "#id")
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project with id " + id + " not found"));
        return toDto(project);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = "projects", allEntries = true)
    public ProjectResponseDto createProject(ProjectCreateRequestDto dto) {

        Project project = Project.builder()
                .title(dto.getTitle())
                .overview(dto.getOverview())
                .duration(dto.getDuration())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .build();
        project = projectRepository.save(project);

        eventPublisher.publishEvent(new ProjectCreatedEvent(project.getId(), project));

        return toDto(project);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "projects", key = "#dto.id")
    @CacheEvict(value = { "projects" }, allEntries = true)
    public ProjectResponseDto updateProject(ProjectUpdateRequestDto dto) {
        Project project = projectRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + dto.getId() + " not found"));

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

        project = projectRepository.save(project);

        eventPublisher.publishEvent(new ProjectUpdatedEvent(project.getId(), project));

        return toDto(project);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = { "projects" }, allEntries = true)
    public void deleteProject(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " not found"));
                
        if (project.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(project.getImageUrl());
        }

        projectRepository.deleteById(id);
        eventPublisher.publishEvent(new ProjectDeletedEvent(id));
    }

    // ===================== Mapper entity -> DTO =====================
    private ProjectResponseDto toDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .overview(project.getOverview())
                .duration(project.getDuration())
                .location(project.getLocation())
                .price(project.getPrice())
                .imageUrl(project.getImageUrl())
                .category(project.getCategory())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
