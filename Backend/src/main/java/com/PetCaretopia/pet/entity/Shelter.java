package com.PetCaretopia.pet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shelters")
@Getter
@Setter
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Shelter name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 255)
    private String location;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid contact number")
    @Column(nullable = true, length = 20)
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @Column(length = 500)
    private String description;

    @Column(nullable = true)
    private String websiteUrl;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;


    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Pet> pets;


}
