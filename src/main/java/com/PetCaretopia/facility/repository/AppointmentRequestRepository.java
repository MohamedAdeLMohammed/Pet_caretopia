package com.PetCaretopia.facility.repository;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByUser_UserID(Long UserId);
    List<AppointmentRequest> findByFacility_Id(Long facilityId);
    List<AppointmentRequest> findByServiceProvider_ServiceProviderID(Long serviceProviderId);
    List<AppointmentRequest> findByStatus(AppointmentRequest.AppointmentRequestStatus status);
}
