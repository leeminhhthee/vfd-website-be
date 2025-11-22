package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TournamentStatusEnum {
    UPCOMING("upcoming"),
    ONGOING("ongoing"),
    ENDED("ended");

    private final String value;

    TournamentStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TournamentStatusEnum fromValue(String value) {
        for (TournamentStatusEnum type : TournamentStatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
