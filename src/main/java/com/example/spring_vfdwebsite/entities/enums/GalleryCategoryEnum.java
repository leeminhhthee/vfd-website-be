package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GalleryCategoryEnum {
    INSIDE("inside"),
    TEAM("team"),
    OTHER("other");

    private final String value;

    GalleryCategoryEnum(String value) {
            this.value = value;
        }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static GalleryCategoryEnum fromValue(String value) {
        for (GalleryCategoryEnum type : GalleryCategoryEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
