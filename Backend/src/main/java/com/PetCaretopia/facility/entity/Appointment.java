package com.PetCaretopia.facility.entity;

import com.PetCaretopia.user.entity.ServiceProvider;
import com.PetCaretopia.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_status", columnList = "status")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //  Linked to a User

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility; //  Linked to a Facility
    @ManyToOne
    @JoinColumn(name = "serviceProvider_id")
    private ServiceProvider serviceProvider;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus ;

    @Column(nullable = true)
    private String reason; //  Allows users to specify a reason
    @Column(nullable = true)
    private Long requestId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public enum AppointmentStatus{
        TREATED,NOT_TREATED
    }
}
