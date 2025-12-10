package com.example.spring_vfdwebsite.entities;

import com.example.spring_vfdwebsite.entities.enums.NewsStatusEnum;
import com.example.spring_vfdwebsite.entities.enums.NewsTypeEnum;

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
@Table(name = "news")
public class News extends BaseEntity {

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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type", nullable = false)
    private NewsTypeEnum type;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NewsStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_by", nullable = true)
    private User authorBy;

    @Size(max = 500)
    @NotNull
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String tags;
}
