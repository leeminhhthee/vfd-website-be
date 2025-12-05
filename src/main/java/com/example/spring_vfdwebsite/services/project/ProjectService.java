package com.example.spring_vfdwebsite.services.project;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectResponseDto;
import com.example.spring_vfdwebsite.dtos.projectDTOs.ProjectUpdateRequestDto;

public interface ProjectService {

    List<ProjectResponseDto> getAllProjects();

    ProjectResponseDto getProjectById(Integer id);

    ProjectResponseDto createProject(ProjectCreateRequestDto dto);

    ProjectResponseDto updateProject(Integer id, ProjectUpdateRequestDto dto);

    void deleteProject(Integer id);

    ProjectResponseDto getProjectBySlug(String slug);
    
}
