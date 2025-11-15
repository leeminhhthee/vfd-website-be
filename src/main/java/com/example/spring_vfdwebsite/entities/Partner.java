package com.example.spring_vfdwebsite.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "partners")
public class Partner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 50)
    @Email
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    @JsonProperty("image_url")
    private String imageUrl;

    @Column(name = "since")
    private LocalDate since;
}
