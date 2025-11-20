package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NewsStatusEnum {
    DRAFT("draft"),
    PUBLISHED("published");

    private final String value;

    NewsStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() { return value; }

    @JsonCreator
    public static NewsStatusEnum fromValue(String value) {
        for (NewsStatusEnum s : values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
