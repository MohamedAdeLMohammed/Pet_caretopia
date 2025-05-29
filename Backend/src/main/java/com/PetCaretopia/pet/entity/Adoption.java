package com.PetCaretopia.pet.entity;

import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Column(nullable = false)
    private LocalDate adoptionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionStatus status;

    private String message;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private PetOwner adopter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_owner_id")
    private PetOwner previousOwner;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "requester_user_id", nullable = false)
    private Long requesterUserId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;


    @ManyToOne
    @JoinColumn(name = "requester_user_id", insertable = false, updatable = false)
    private User requesterUser;
}
