package com.PetCaretopia.facility.repository;

import com.PetCaretopia.facility.entity.Appointment;
import com.PetCaretopia.facility.entity.AppointmentStatus;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser(User user); // Get appointments by user

    List<Appointment> findByFacilityId(Long facilityId); // Get appointments by facility

    List<Appointment> findByStatus(AppointmentStatus status); // Get appointments by status

    List<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end); // Get appointments in a time range
}
