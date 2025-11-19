package com.example.spring_vfdwebsite.entities;

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
@Table(name = "projects")
public class Project extends BaseEntity {

    public enum ProjectCategory {
        PHAT_TRIEN("development"),
        CO_SO_HA_TANG("infrastructure"),
        HOP_TAC("collaboration"),
        DAO_TAO("training"),
        CONG_DONG("community");

        private final String value;

        ProjectCategory(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static ProjectCategory fromValue(String value) {
            for (ProjectCategory type : ProjectCategory.values()) {
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

    @Size(max = 255)
    @NotNull
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "overview")
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
    private ProjectCategory category;
}
