package com.example.spring_vfdwebsite.services.boardDirector;

import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorResponseDto;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.boardDirectorDTOs.BoardDirectorUpdateRequestDto;

import java.util.List;

public interface BoardDirectorService {

    /**
     * Create a new board director.
     * @param createDto DTO containing information to create a new board director
     * @return BoardDirectorResponseDto of the created board director
     */
    BoardDirectorResponseDto createBoardDirector(BoardDirectorCreateRequestDto createDto);

    /**
     * Retrieve all board directors.
     * @return list of BoardDirectorResponseDto
     */
    List<BoardDirectorResponseDto> getAllBoardDirectors();

    /**
     * Retrieve a board director by ID.
     * @param id the ID of the board director
     * @return BoardDirectorResponseDto of the board director
     */
    BoardDirectorResponseDto getBoardDirectorById(Integer id);

    /**
     * Update an existing board director.
     * @param updateDto DTO containing updated information
     * @return BoardDirectorResponseDto of the updated board director
     */
    BoardDirectorResponseDto updateBoardDirector(BoardDirectorUpdateRequestDto updateDto);

    /**
     * Delete a board director by ID.
     * @param id the ID of the board director to delete
     */
    void deleteBoardDirector(Integer id);
}
