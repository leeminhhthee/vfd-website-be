package com.example.spring_vfdwebsite.entities;

import java.time.LocalDateTime;

import com.example.spring_vfdwebsite.entities.enums.DraftStatusEnum;
import com.example.spring_vfdwebsite.entities.enums.RoundEnum;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "match_schedule_drafts")
public class MatchScheduleDraft extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "round")
    private RoundEnum round;

    @Column(name = "group_table")
    private String groupTable;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    @Column(name = "team_A")
    private String teamA;

    @Column(name = "team_B")
    private String teamB;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DraftStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
}
