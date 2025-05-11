package com.PetCaretopia.pet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "pet_breeds",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"breed_name", "pet_type_id"})
        }
)
@Getter
@Setter
public class PetBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Breed name is required")
    @Column(nullable = false, length = 50)
    private String breedName;

    @ManyToOne
    @JoinColumn(name = "pet_type_id", nullable = false)
    private PetType petType;
}
