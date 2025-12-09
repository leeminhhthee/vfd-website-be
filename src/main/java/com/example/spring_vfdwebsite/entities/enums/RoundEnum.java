package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoundEnum {
    GROUP("group"),
    ROUND_OF_16("round-of-16"),
    QUARTER_FINAL("quarter-final"),
    SEMI_FINAL("semi-final"),
    THIRD_PLACE("third-place"),
    FINAL("final");

    private final String value;

    RoundEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoundEnum fromValue(String value) {
        for (RoundEnum type : RoundEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
