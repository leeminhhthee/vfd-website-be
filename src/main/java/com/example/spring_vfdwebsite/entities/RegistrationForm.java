package com.example.spring_vfdwebsite.entities;

import com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "registration_forms", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class RegistrationForm extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Email
    @NotNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "team_name", length = 100, nullable = false)
    private String teamName;

    @Size(max = 255)
    @NotNull
    @Column(name = "registration_unit", length = 255, nullable = false)
    private String registrationUnit;

    @NotNull
    @Column(name = "number_athletes", nullable = false)
    private Integer numberAthletes;

    @Size(max = 500)
    @NotNull
    @Column(name = "file_url", length = 500, nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RegistrationStatusEnum status;
}
