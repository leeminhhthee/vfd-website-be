package com.example.spring_vfdwebsite.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
import com.example.spring_vfdwebsite.services.user.UserServiceImpl;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CloudinaryUtils cloudinaryUtils;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setFullName("Test User");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPhoneNumber("0912345645");
        mockUser.setPassword("password123");
        mockUser.setAdmin(true);
        mockUser.setActive(true);
        mockUser.setImageUrl("http://image.url/test.jpg");
    }

    // Test for getting all users
    @Test
    void testGetAllUsers_ShouldReturnListOfUsers() {
        // Test implementation will go here
        // (Given phase) Thiet lap du lieu va hanh vi
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        // Goi ham that
        List<UserResponseDto> result = userService.getAllUsers();

        // THEN phase: Kiem tra ket qua
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getFullName());

        // AND phase: Xac minh hanh vi
        verify(userRepository, times(1)).findAll();
    }

    // Test for getting a user by ID
    @Test
    void testGetUserById_ShouldReturnUser() {

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        UserResponseDto result = userService.getUserById(1);

        assertEquals("Test User", result.getFullName());
        assertEquals("testuser@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(1);
    }

    // Test for getting a user by ID when user is not found
    @Test
    void testGetUserById_UserNotFound_ShouldThrowException() {

        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(2));

        verify(userRepository, times(1)).findById(2);

    }

    // Test for creating a new admin user
    @Test
    void createUser_AdminUser_Success() {
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setFullName("Test User");
        dto.setEmail("testuser@example.com");
        dto.setPhoneNumber("0912345645");
        dto.setIsAdmin(true);
        dto.setIsActive(true);
        dto.setPassword("admin123");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        UserResponseDto result = userService.createUser(dto);

        assertEquals("Test User", result.getFullName());
        assertTrue(result.getIsAdmin());
        assertTrue(result.getIsActive());

        // verify repository interactions
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        // verify event published
        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(1, eventCaptor.getValue().getUserId());
    }

    // Test for creating a new non-admin user
    @Test
    void createUser_NonAdminUser_Success() {
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setFullName("Normal User");
        dto.setEmail("normal@example.com");
        dto.setIsAdmin(false);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(2);
            return u;
        });

        UserResponseDto result = userService.createUser(dto);

        assertEquals("Normal User", result.getFullName());
        assertFalse(result.getIsAdmin());

        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserCreatedEvent.class));
    }

    // Test for creating a user with duplicate email
    @Test
    void createUser_DuplicateEmail_ShouldThrowException() {
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setEmail("testuser@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(mockUser));

        assertThrows(EntityDuplicateException.class, () -> userService.createUser(dto));

        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void createUser_AdminWithoutPassword_ShouldThrowHttpException() {
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setEmail("admin@example.com");
        dto.setIsAdmin(true);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(HttpException.class, () -> userService.createUser(dto));

        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    // Test for updating a user
    @Test
    void updateUser_AllFields_Success() {
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        dto.setFullName("Updated Name");
        dto.setEmail("updated@example.com");
        dto.setPhoneNumber("0999999999");
        dto.setIsAdmin(true);
        dto.setIsActive(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto result = userService.updateUser(1, dto);

        assertEquals("Updated Name", result.getFullName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("0999999999", result.getPhoneNumber());
        assertTrue(result.getIsAdmin());
        assertFalse(result.getIsActive());

        // verify repository & event
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByEmail("updated@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserUpdatedEvent.class));
    }

    // Test for updating a user with duplicate email
    @Test
    void updateUser_DuplicateEmail_ShouldThrowException() {
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        dto.setEmail("duplicate@example.com");

        User anotherUser = new User();
        anotherUser.setId(2); // khác id mockUser
        anotherUser.setEmail("duplicate@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(Optional.of(anotherUser));

        assertThrows(EntityDuplicateException.class, () -> userService.updateUser(1, dto));

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByEmail("duplicate@example.com");
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    // Test for downgrading an admin to non-admin
    @Test
    void updateUser_DowngradeAdmin_ShouldClearPassword() {
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        dto.setIsAdmin(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto result = userService.updateUser(1, dto);

        assertFalse(result.getIsAdmin());
        assertEquals("", mockUser.getPassword()); // password bị xóa

        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserUpdatedEvent.class));
    }

    // Test for updating a non-existing user
    @Test
    void updateUser_UserNotFound_ShouldThrowException() {
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        dto.setFullName("New Name");

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(99, dto));

        verify(userRepository, times(1)).findById(99);
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    // Test for deleting a user
    @Test
    void deleteUser_WithImage_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(1);

        // verify repository delete
        verify(userRepository, times(1)).deleteById(1);

        // verify cloudinary delete
        verify(cloudinaryUtils, times(1)).deleteFile("http://image.url/test.jpg");

        // verify event published
        verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class));
    }

    // Test for deleting a user without image
    @Test
    void deleteUser_WithoutImage_Success() {
        mockUser.setImageUrl(null); // Không có ảnh
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(1);

        // verify repository delete
        verify(userRepository, times(1)).deleteById(1);

        // verify cloudinary delete không gọi
        verify(cloudinaryUtils, never()).deleteFile(any());

        // verify event published
        verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class));
    }

    // Test for deleting a non-existing user
    @Test
    void deleteUser_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(99));

        verify(userRepository, times(1)).findById(99);
        verify(userRepository, never()).deleteById(anyInt());
        verify(cloudinaryUtils, never()).deleteFile(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

}
