package com.example.spring_vfdwebsite.dtos.authDTOs;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInitRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @AssertTrue(message = "Either username or email is required")
    public boolean isUsernameOrEmailProvided() {
        return (email != null && !email.isBlank());
    }

    private String fullName;

    private String phoneNumber;
}
