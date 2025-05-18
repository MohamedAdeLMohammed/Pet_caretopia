package com.PetCaretopia.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.PetCaretopia.user.entity.ServiceProvider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    Optional<ServiceProvider> findByUser_UserID(Long userID);
    void deleteByUser_UserID(Long userId);

    List<ServiceProvider> findByServiceProviderType(ServiceProvider.ServiceProviderType type);
}
