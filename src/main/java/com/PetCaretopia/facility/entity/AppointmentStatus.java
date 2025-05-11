package com.PetCaretopia.facility.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum AppointmentStatus {
    PENDING("Pending"),
    SCHEDULED("Scheduled"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    NO_SHOW("No Show");

    private final String displayName;

    // Reverse lookup map for quick conversion from string
    private static final Map<String, AppointmentStatus> STATUS_MAP = new HashMap<>();

    static {
        for (AppointmentStatus status : values()) {
            STATUS_MAP.put(status.displayName.toLowerCase(), status);
        }
    }

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static AppointmentStatus fromString(String value) {
        return STATUS_MAP.getOrDefault(value.toLowerCase(), null); // Returns null if not found
    }
}
