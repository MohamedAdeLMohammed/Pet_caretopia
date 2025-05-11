package com.PetCaretopia.health.entity;

import com.PetCaretopia.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "breeding_records",
        indexes = {
                @Index(name = "idx_breeding_male_pet", columnList = "male_pet_id"),
                @Index(name = "idx_breeding_female_pet", columnList = "female_pet_id")
        })
@Getter
@Setter
public class Breeding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "male_pet_id", nullable = false)
    private Pet malePet; // Male Parent

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "female_pet_id", nullable = false)
    private Pet femalePet; // Female Parent

    @Column(nullable = false)
    private LocalDate breedingDate; // Breeding date

    @Column(nullable = true)
    private LocalDate expectedBirthDate; // Expected birth date

    @Column(nullable = true)
    private Integer numberOfOffspring; // Number of puppies/kittens produced

    @Column(length = 500)
    private String notes; // Additional information

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
