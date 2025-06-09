package com.PetCaretopia.facility.repository;

import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser_UserID(Long UserId);
    List<Appointment> findByFacility_Id(Long facilityId);
    List<Appointment> findByServiceProvider_ServiceProviderID(Long serviceProviderId);

    List<Appointment> findByAppointmentStatus(Appointment.AppointmentStatus status); // Get appointments by status

    boolean existsByUser_UserIDAndFacility_IdAndServiceProvider_ServiceProviderIDAndAppointmentTimeBetween(
            Long userId, Long facilityId, Long serviceProviderId,
            LocalDateTime startOfDay, LocalDateTime endOfDay
    );
    Optional<Appointment> findByRequestId(Long requestId);
}
