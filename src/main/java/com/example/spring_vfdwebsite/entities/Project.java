package com.example.spring_vfdwebsite.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.spring_vfdwebsite.entities.enums.ProjectCategoryEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Lob
    @Column(name = "overview", columnDefinition = "LONGTEXT")
    private String overview;

    @Size(max = 100)
    @Column(name = "duration", length = 100)
    private String duration;

    @Size(max = 255)
    @Column(name = "location", length = 255)
    private String location;

    @Size(max = 100)
    @Column(name = "price", length = 100)
    private String price;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    @JsonProperty("image")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "category", nullable = false)
    private ProjectCategoryEnum category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
