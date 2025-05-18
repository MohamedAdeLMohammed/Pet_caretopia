package com.PetCaretopia.facility.service;

import com.PetCaretopia.facility.repository.AppointmentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentRequestService {
    private final AppointmentRequestRepository appointmentRequestRepository;

}
