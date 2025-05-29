package com.PetCaretopia.facility.event;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import com.PetCaretopia.facility.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentGeneratorService {
    private final AppointmentService appointmentService;
    @EventListener
    public void onAppointmentRequestAccepted(AppointmentRequestAcceptedEvent event) {
        // Optionally: Use the accepted request from the event if needed
        AppointmentRequest acceptedRequest = event.getAcceptedRequest();
        appointmentService.generateAppointment(acceptedRequest);
    }
}
