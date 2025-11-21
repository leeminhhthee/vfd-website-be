package com.example.spring_vfdwebsite.events.registrationForm;

import com.example.spring_vfdwebsite.entities.RegistrationForm;

import lombok.Value;

@Value
public class RegistrationFormUpdatedEvent {
    private final Integer registrationFormId;
    private final RegistrationForm updatedRegistrationForm;
}
