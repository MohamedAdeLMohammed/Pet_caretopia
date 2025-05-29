package com.PetCaretopia.health.entity;

import jakarta.persistence.*;
import lombok.*;
import com.PetCaretopia.pet.entity.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_vaccines",
        indexes = {
                @Index(name = "idx_pet_vaccine_pet", columnList = "pet_id"),
                @Index(name = "idx_pet_vaccine_vaccine", columnList = "vaccine_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetVaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet; // Linked to a Pet

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private Vaccine vaccine; //  Linked to a Vaccine

    @Column(nullable = false)
    private LocalDate vaccinationDate; // Date when the vaccine was given

    @Column(nullable = true)
    private LocalDate nextDoseDue; //  Date for the next dose (if applicable)

    @Column(length = 500)
    private String notes; //  Additional notes about vaccination

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
