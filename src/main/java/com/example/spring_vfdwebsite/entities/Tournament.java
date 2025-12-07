package com.example.spring_vfdwebsite.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.spring_vfdwebsite.entities.enums.TournamentStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "tournaments")
public class Tournament extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "slug", length = 255, unique = true)
    private String slug;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 255)
    @Column(name = "location", length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false)
    private TournamentStatusEnum status;

    @Column(name = "is_visible_on_home")
    private Boolean isVisibleOnHome;

    @Column(name = "registration_open")
    private Boolean registrationOpen;

    @Size(max = 500)
    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @ElementCollection
    @CollectionTable(name = "tournament_schedule_images", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "image_url")
    private List<String> scheduleImages;

    // @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade =
    // CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore
    // @Builder.Default
    // private List<TournamentDocument> tournamentDocuments = new ArrayList<>();

    // @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade =
    // CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore
    // @Builder.Default
    // private List<MatchSchedule> matchSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<TournamentDocument> tournamentDocuments = new HashSet<>();

    @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<MatchSchedule> matchSchedules = new HashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationForm> registrationForms;

}
