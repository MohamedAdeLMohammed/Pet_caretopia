package com.PetCaretopia.facility.event;

import com.PetCaretopia.facility.entity.AppointmentRequest;
import org.springframework.context.ApplicationEvent;



public class AppointmentRequestAcceptedEvent extends ApplicationEvent {
    private final AppointmentRequest acceptedRequest;

    public AppointmentRequestAcceptedEvent(Object source, AppointmentRequest acceptedRequest) {
        super(source);
        this.acceptedRequest = acceptedRequest;
    }

    public AppointmentRequest getAcceptedRequest() {
        return acceptedRequest;
    }
}