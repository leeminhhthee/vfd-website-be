package com.example.spring_vfdwebsite.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

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

    public enum GalleryCategory {
        INSITE("insite"),
        TEAM("team"),
        OTHER("other");

        private final String value;

        GalleryCategory(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static GalleryCategory fromValue(String value) {
            for (GalleryCategory type : GalleryCategory.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid category: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "category", nullable = false)
    private GalleryCategory category;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(
            name = "gallery_images",
            joinColumns = @JoinColumn(name = "gallery_id")
    )
    @Column(name = "image_url")
    @JsonProperty("images")
    private List<String> imageUrl;
}
