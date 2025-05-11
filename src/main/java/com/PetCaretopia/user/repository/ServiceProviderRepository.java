package com.PetCaretopia.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.PetCaretopia.user.entity.ServiceProvider;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    Optional<ServiceProvider> findByUser_UserID(Long userID);
    List<ServiceProvider> findByServiceProviderType(String serviceProviderType);
}
