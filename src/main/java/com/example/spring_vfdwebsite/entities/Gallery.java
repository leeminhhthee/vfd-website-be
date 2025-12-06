package com.example.spring_vfdwebsite.entities;

import java.util.List;

import com.example.spring_vfdwebsite.entities.enums.GalleryCategoryEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "galleries")
public class Gallery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "category", nullable = false)
    private GalleryCategoryEnum category;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @ElementCollection
    @CollectionTable(name = "gallery_images", joinColumns = @JoinColumn(name = "gallery_id"))
    @Column(name = "image_url")
    @JsonProperty("images")
    private List<String> imageUrl;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "tournament_id", nullable = true)
    @JsonIgnore
    private Tournament tournament;
}
