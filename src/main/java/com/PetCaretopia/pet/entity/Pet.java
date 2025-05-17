package com.PetCaretopia.pet.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.PetCaretopia.user.entity.PetOwner;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petID;

    @Column(nullable = false)
    private String petName;

    @ManyToOne
    @JoinColumn(name = "pet_owner_id", nullable = true)
    private PetOwner owner;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = true)
    private Shelter shelter;

    @ManyToOne
    @JoinColumn(name = "pet_type_id", nullable = false)
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "pet_breed_id", nullable = false)
    private PetBreed petBreed;

    @Column(name = "is_available_for_adoption", nullable = false)
    private boolean isAvailableForAdoption = false;


    @Column(length = 500)
    private String imageUrl;

    public void adopt(PetOwner newOwner) {
        this.owner = newOwner; //Update owner when adopted
        this.shelter = null; //If it was in a shelter, remove it
    }
}
