package com.PetCaretopia.facility.repository;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByUser(User user); // Get requests by user

    List<AppointmentRequest> findByFacilityId(Long facilityId); // Get requests by facility

    List<AppointmentRequest> findByStatus(AppointmentRequest.AppointmentRequestStatus status); // Get requests by status
}
