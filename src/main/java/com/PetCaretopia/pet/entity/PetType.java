package com.PetCaretopia.pet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pet_types")
@Getter
@Setter
public class PetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pet type is required")
    @Column(nullable = false, unique = true, length = 50)
    private String typeName;
}
