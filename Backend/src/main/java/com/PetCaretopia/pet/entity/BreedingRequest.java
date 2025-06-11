package com.PetCaretopia.pet.entity;

import com.PetCaretopia.user.entity.PetOwner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class BreedingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pet malePet;

    @ManyToOne
    private Pet femalePet;

    @ManyToOne
    private PetOwner requester;

    @ManyToOne
    private PetOwner receiver;

    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    private BreedingStatus status; // PENDING, ACCEPTED, REJECTED
}
