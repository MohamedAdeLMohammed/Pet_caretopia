package com.PetCaretopia.health.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vaccines",
        indexes = {@Index(name = "idx_vaccine_name", columnList = "name")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name; //   Vaccine Name (e.g., Rabies, Parvovirus)

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private VaccineType type; //   Linked to VaccineType

    @Column(length = 500)
    private String description; //   Short description

    @Column(nullable = false)
    private Integer recommendedAgeWeeks; //   Age at which it should be administered (in weeks)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
