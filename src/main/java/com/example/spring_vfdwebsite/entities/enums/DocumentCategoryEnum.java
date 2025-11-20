package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentCategoryEnum {
    PLAN("plan"),
        CHARTER("charter"),
        FORMS("forms"),
        REGULATIONS("regulations");

        private final String value;

        DocumentCategoryEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static DocumentCategoryEnum fromValue(String value) {
            for (DocumentCategoryEnum type : DocumentCategoryEnum.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid category: " + value);
        }
}
