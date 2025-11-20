package com.example.spring_vfdwebsite.services.user;

import com.example.spring_vfdwebsite.dtos.userDTOs.UserCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.userDTOs.UserResponseDto;
import com.example.spring_vfdwebsite.dtos.userDTOs.UserUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.events.users.UserCreatedEvent;
import com.example.spring_vfdwebsite.events.users.UserDeletedEvent;
import com.example.spring_vfdwebsite.events.users.UserUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityDuplicateException;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.exceptions.HttpException;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final CloudinaryUtils cloudinaryUtils;

    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    // Convert entity to DTO
    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl())
                .hobby(user.getHobby())
                .isAdmin(user.isAdmin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Create new user
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto createUser(UserCreateRequestDto createDto) {

        // Check email uniqueness
        if (!userRepository.findByEmail(createDto.getEmail()).isEmpty()) {
            throw new EntityDuplicateException("User");
        }

        // Business rule: password is required only when creating an admin user
        if (Boolean.TRUE.equals(createDto.getIsAdmin())) {
            if (createDto.getPassword() == null || createDto.getPassword().isBlank()) {
                throw new HttpException("Password is required when creating an admin user",
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        }

        // Build entity without password first to avoid accidental overwrite
        User user = User.builder()
                .fullName(createDto.getFullName())
                .email(createDto.getEmail())
                .phoneNumber(createDto.getPhoneNumber())
                .imageUrl(createDto.getImageUrl())
                .hobby(createDto.getHobby())
                .build();

        // Ensure admin flag is applied explicitly on the entity (avoids builder
        // naming/boxing pitfalls)
        user.setAdmin(Boolean.TRUE.equals(createDto.getIsAdmin()));

        // Apply password only for admin users; non-admin users will have an empty
        // password
        // Store passwords encoded using BCrypt
        if (Boolean.TRUE.equals(createDto.getIsAdmin())) {
            user.setPassword(passwordEncoder.encode(createDto.getPassword()));
        } else {
            user.setPassword("");
        }

        user = userRepository.save(user);

        // Phát sự kiện UserCreatedEvent
        eventPublisher.publishEvent(new UserCreatedEvent(user.getId(), user));
        return toDto(user);
    }

    // Get all users
    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<UserResponseDto> getAllUsers() {
        System.out.println("🔥 Fetching all users from the database...");
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Get user by ID
    @Override
    @Cacheable(value = "users", key = "#root.args[0]")
    public UserResponseDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id));
    }

    // Update user
    @Override
    @CachePut(value = "users", key = "#p0")
    @CacheEvict(value = "users", key = "'all'")
    public UserResponseDto updateUser(Integer id, UserUpdateRequestDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        if (updateDto.getEmail() != null) {
            List<User> found = userRepository.findByEmail(updateDto.getEmail());
            if (!found.isEmpty() && !found.get(0).getId().equals(id)) {
                throw new EntityDuplicateException("User");
            }
            user.setEmail(updateDto.getEmail());
        }
        if (updateDto.getFullName() != null)
            user.setFullName(updateDto.getFullName());
        if (updateDto.getPhoneNumber() != null)
            user.setPhoneNumber(updateDto.getPhoneNumber());
        if (updateDto.getImageUrl() != null)
            user.setImageUrl(updateDto.getImageUrl());
        if (updateDto.getHobby() != null)
            user.setHobby(updateDto.getHobby());
        if (updateDto.getPassword() != null)
            user.setPassword(updateDto.getPassword());
        if (updateDto.getIsAdmin() != null) {
            user.setAdmin(updateDto.getIsAdmin());
            // If user is downgraded to non-admin, clear password to align with member
            // behavior
            if (!user.isAdmin()) {
                user.setPassword("");
            }
        }

        user = userRepository.save(user);

        // Phát sự kiện UserUpdatedEvent
        eventPublisher.publishEvent(new UserUpdatedEvent(user.getId(), user));
        return toDto(user);
    }

    // Delete user
    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id));

        if (user.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(user.getImageUrl());
        }
        userRepository.deleteById(id);

        // Phát sự kiện UserDeletedEvent
        eventPublisher.publishEvent(new UserDeletedEvent(id));
    }
}
