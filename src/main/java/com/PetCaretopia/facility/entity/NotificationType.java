package com.PetCaretopia.facility.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    APPOINTMENT_CONFIRMED("Appointment Confirmed"),
    APPOINTMENT_CANCELED("Appointment Canceled"),
    APPOINTMENT_REQUEST_APPROVED("Appointment Request Approved"),
    APPOINTMENT_REQUEST_REJECTED("Appointment Request Rejected"),
    REMINDER("Appointment Reminder"), // ✅ Optional: Reminder Notification
    SYSTEM_ALERT("System Alert"); // ✅ Optional: General Alerts

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }
}
