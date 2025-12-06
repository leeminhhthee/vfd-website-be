package com.example.spring_vfdwebsite.services.boardDirector;

import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorResponseDto;
import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.BoardDirector;
import com.example.spring_vfdwebsite.exceptions.EntityDuplicateException;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.BoardDirectorJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardDirectorServiceImpl implements BoardDirectorService {

    private final BoardDirectorJpaRepository boardDirectorRepository;
    private final CloudinaryUtils cloudinaryUtils;

    // Convert entity to DTO
    private BoardDirectorResponseDto toDto(BoardDirector director) {
        return BoardDirectorResponseDto.builder()
                .id(director.getId())
                .fullName(director.getFullName())
                .email(director.getEmail())
                .phoneNumber(director.getPhoneNumber())
                .role(director.getRole())
                .term(director.getTerm())
                .bio(director.getBio())
                .imageUrl(director.getImageUrl())
                .createdAt(director.getCreatedAt())
                .updatedAt(director.getUpdatedAt())
                .build();
    }

    // Create new board director
    @Override
    @CacheEvict(value = "board-directors", key = "'all'")
    @LoggableAction(value =  "CREATE", entity = "board_directors", description = "Create new board director")
    public BoardDirectorResponseDto createBoardDirector(BoardDirectorCreateRequestDto createDto) {

        // Check email uniqueness
        if (!boardDirectorRepository.findByEmail(createDto.getEmail()).isEmpty()) {
            throw new EntityDuplicateException("BoardDirector");
        }

        BoardDirector director = BoardDirector.builder()
                .fullName(createDto.getFullName())
                .email(createDto.getEmail())
                .phoneNumber(createDto.getPhoneNumber())
                .role(createDto.getRole())
                .term(createDto.getTerm())
                .bio(createDto.getBio())
                .imageUrl(createDto.getImageUrl())
                .build();

        director = boardDirectorRepository.save(director);
        return toDto(director);
    }

    // Get all directors
    @Override
    @Cacheable(value = "board-directors", key = "'all'")
    public List<BoardDirectorResponseDto> getAllBoardDirectors() {
        System.out.println("🔥 Fetching all board directors from the database...");
        return boardDirectorRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Get director by ID
    @Override
    @Cacheable(value = "board-directors", key = "#root.args[0]")
    public BoardDirectorResponseDto getBoardDirectorById(Integer id) {
        return boardDirectorRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("BoardDirector with id " + id));
    }

    // Update director
    @Override
    @CachePut(value = "board-directors", key = "#p0")
    @CacheEvict(value = "board-directors", key = "'all'")
    @LoggableAction(value =  "UPDATE", entity = "board_directors", description = "Update board director")
    public BoardDirectorResponseDto updateBoardDirector(Integer id, BoardDirectorUpdateRequestDto updateDto) {
        BoardDirector director = boardDirectorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardDirector with id " + id));
        if (updateDto.getEmail() != null) {
            List<BoardDirector> found = boardDirectorRepository.findByEmail(updateDto.getEmail());
            if (!found.isEmpty() && !found.get(0).getId().equals(id)) {
                throw new EntityDuplicateException("BoardDirector");
            }
            director.setEmail(updateDto.getEmail());
        }
        if (updateDto.getFullName() != null)
            director.setFullName(updateDto.getFullName());
        if (updateDto.getEmail() != null) {
            List<BoardDirector> found = boardDirectorRepository.findByEmail(updateDto.getEmail());
            if (!found.isEmpty() && !found.get(0).getId().equals(updateDto.getId())) {
                throw new EntityDuplicateException("BoardDirector");
            }
            director.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPhoneNumber() != null)
            director.setPhoneNumber(updateDto.getPhoneNumber());
        if (updateDto.getRole() != null)
            director.setRole(updateDto.getRole());
        if (updateDto.getTerm() != null)
            director.setTerm(updateDto.getTerm());
        if (updateDto.getBio() != null)
            director.setBio(updateDto.getBio());
        if (updateDto.getImageUrl() != null)
            director.setImageUrl(updateDto.getImageUrl());

        director = boardDirectorRepository.save(director);
        return toDto(director);
    }

    // Delete director
    @Override
    @CacheEvict(value = "board-directors", allEntries = true)
    @LoggableAction(value =  "DELETE", entity = "board_directors", description = "Delete board director")
    public void deleteBoardDirector(Integer id) {
        BoardDirector director = boardDirectorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardDirector with id " + id));
                
        if (director.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(director.getImageUrl());
        }
        
        boardDirectorRepository.deleteById(id);
    }
}