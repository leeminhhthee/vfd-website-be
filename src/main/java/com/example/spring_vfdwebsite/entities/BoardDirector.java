package com.example.spring_vfdwebsite.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_directors", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class BoardDirector extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "phone_number", length = 10)
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull
    @Column(name = "role", nullable = false, length = 255)
    private String role;

    @NotNull
    @Column(name = "term", nullable = false, length = 255)
    private String term;

    @Lob
    @Column(name = "bio", columnDefinition = "LONGTEXT")
    private String bio;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
