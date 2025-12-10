package com.example.spring_vfdwebsite.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "config_key", nullable = false, unique = true)
    private String key;

    @Column(name = "config_value")
    private String value;
}
