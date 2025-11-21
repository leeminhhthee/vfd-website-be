package com.example.spring_vfdwebsite.events.registrationForm;

import lombok.Value;

@Value
public class RegistrationFormDeletedEvent {
    private final Integer registrationFormId;
}