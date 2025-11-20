package com.example.spring_vfdwebsite.services.user;

import com.example.spring_vfdwebsite.dtos.userDTOs.UserCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.userDTOs.UserResponseDto;
import com.example.spring_vfdwebsite.dtos.userDTOs.UserUpdateRequestDto;

import java.util.List;

public interface UserService {

	/**
	 * Create a new user.
	 * @param createDto DTO containing information to create a new user
	 * @return UserResponseDto of the created user
	 */
	UserResponseDto createUser(UserCreateRequestDto createDto);

	/**
	 * Retrieve all users.
	 * @return list of UserResponseDto
	 */
	List<UserResponseDto> getAllUsers();

	/**
	 * Retrieve a user by ID.
	 * @param id the ID of the user
	 * @return UserResponseDto of the user
	 */
	UserResponseDto getUserById(Integer id);

	/**
	 * Update an existing user.
	 * @param updateDto DTO containing updated information
	 * @return UserResponseDto of the updated user
	 */
	UserResponseDto updateUser(Integer id, UserUpdateRequestDto updateDto);

	/**
	 * Delete a user by ID.
	 * @param id the ID of the user to delete
	 */
	void deleteUser(Integer id);

}
