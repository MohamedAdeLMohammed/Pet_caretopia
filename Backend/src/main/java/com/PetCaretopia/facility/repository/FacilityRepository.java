package com.PetCaretopia.facility.repository;

import com.PetCaretopia.facility.entity.Facility;
import com.PetCaretopia.facility.entity.FacilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findByName(String name); // Find facility by name

    List<Facility> findByStatus(FacilityStatus status);// Get facilities by status
    List<Facility> findByFacilityType(Facility.FacilityType type);
    //@Query("SELECT f FROM Facility f JOIN f.serviceProviders sp WHERE sp.serviceProviderID = :serviceProviderId")
    List<Facility> findFacilitiesByServiceProvider_ServiceProviderID(@Param("serviceProviderId") Long serviceProviderId);
}
