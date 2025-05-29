package com.PetCaretopia.facility.entity;

import com.PetCaretopia.user.entity.ServiceProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "facilities",
        indexes = {
                @Index(name = "idx_facility_name", columnList = "name"),
                @Index(name = "idx_facility_status", columnList = "status")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @ManyToMany(mappedBy = "facilities",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceProvider> serviceProviders;
    @Column(nullable = false)
    private FacilityType facilityType;
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    public enum FacilityType{
        VETERINARY_CLINIC,PET_HOTEL,TRAINING_CENTER
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

