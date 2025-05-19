package com.PetCaretopia.facility.entity;

import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //   Linked to User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;// Linked to Facility

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceProvider_id" , nullable = false)
    private ServiceProvider serviceProvider;

    @Column(nullable = false)
    private LocalDateTime requestedTime;

    @Column(columnDefinition = "TEXT")
    private String reason; //   Allows long request reason

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentRequestStatus status; //   Enum for Status

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
    public enum AppointmentRequestStatus{
        PENDING,ACCEPTED,REJECTED,CANCELED
    }
}
