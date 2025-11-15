package com.example.spring_vfdwebsite.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "heroes")
public class Hero extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "sub_title")
    private String subTitle;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    @JsonProperty("image")
    private String imageUrl;

    @Size(max = 100)
    @Column(name = "button_text", length = 100)
    private String buttonText;

    @Size(max = 500)
    @Column(name = "button_href", length = 500)
    private String buttonHref;

    @Size(max = 100)
    @Column(name = "button_text_2", length = 100)
    private String buttonText2;

    @Size(max = 500)
    @Column(name = "button_href_2", length = 500)
    private String buttonHref2;
}
