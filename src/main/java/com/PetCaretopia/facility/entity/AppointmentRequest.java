package com.PetCaretopia.facility.entity;

import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_requests",
        indexes = {
                @Index(name = "idx_appointment_request_user", columnList = "user_id"),
                @Index(name = "idx_appointment_request_facility", columnList = "facility_id"),
                @Index(name = "idx_appointment_request_status", columnList = "status")
        })
@Getter
@Setter
public class AppointmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //   Linked to User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility; //   Linked to Facility

    @Column(nullable = false)
    private LocalDateTime requestedTime;

    @Column(columnDefinition = "TEXT")
    private String reason; //   Allows long request reason

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING; //   Enum for Status

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt; //   Tracks updates

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
