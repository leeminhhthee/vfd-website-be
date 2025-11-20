package com.example.spring_vfdwebsite.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectCategoryEnum {
        PHAT_TRIEN("development"),
        CO_SO_HA_TANG("infrastructure"),
        HOP_TAC("collaboration"),
        DAO_TAO("training"),
        CONG_DONG("community");

        private final String value;

        ProjectCategoryEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static ProjectCategoryEnum fromValue(String value) {
            for (ProjectCategoryEnum type : ProjectCategoryEnum.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid category: " + value);
        }
    }
