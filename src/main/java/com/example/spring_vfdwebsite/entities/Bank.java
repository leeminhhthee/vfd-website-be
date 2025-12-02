package com.example.spring_vfdwebsite.entities;

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
@Table(name = "banks")
public class Bank extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 100)
    @NotNull
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @NotNull
    @Column(name = "account_number", nullable = false, length = 20)
    // @Pattern(regexp = "\\d{10,20}", message = "Account number must be between 10 and 20 digits")
    private String accountNumber;

    @Size(max = 255)
    @Column(name = "branch", length = 255)
    private String branch;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
