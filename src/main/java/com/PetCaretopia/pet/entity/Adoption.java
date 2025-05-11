package com.PetCaretopia.pet.entity;


import com.PetCaretopia.user.entity.PetOwner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDate;

@Entity
@Table(name = "adoptions")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"pet", "adopter", "shelter"})
@ToString(exclude = {"pet", "adopter", "shelter"})
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "adopter_id", nullable = false)
    private PetOwner adopter;


    @ManyToOne
    @JoinColumn(name = "previous_owner_id", nullable = true)
    private PetOwner previousOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = true)
    private Shelter shelter;

    @Column(nullable = false)
    private LocalDate adoptionDate;

    @Column(nullable = false)
    private Boolean isApproved = false;
}
