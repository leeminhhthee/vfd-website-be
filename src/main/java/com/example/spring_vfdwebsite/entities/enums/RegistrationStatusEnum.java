package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegistrationStatusEnum {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String value;

    RegistrationStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() { return value; }

    @JsonCreator
    public static RegistrationStatusEnum fromValue(String value) {
        for (RegistrationStatusEnum s : values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
