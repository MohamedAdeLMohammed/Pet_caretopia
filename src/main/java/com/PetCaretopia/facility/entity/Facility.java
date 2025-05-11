package com.PetCaretopia.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "facilities",
        indexes = {
                @Index(name = "idx_facility_name", columnList = "name"),
                @Index(name = "idx_facility_status", columnList = "status")
        })
@Getter
@Setter
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String address; //   Facility Location

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityStatus status = FacilityStatus.ACTIVE; //   ACTIVE, INACTIVE

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

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
