package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NewsTypeEnum {
    CITY("city"),
    INSIDE_VN("inside_vn"),
    INTERNATIONAL("international"),
    OTHER("other");

    private final String value;

    NewsTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NewsTypeEnum fromValue(String value) {
        for (NewsTypeEnum type : NewsTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + value);
    }
}