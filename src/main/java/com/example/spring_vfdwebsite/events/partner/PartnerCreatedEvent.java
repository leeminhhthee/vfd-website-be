package com.example.spring_vfdwebsite.events.partner;

import com.example.spring_vfdwebsite.entities.Partner;

import lombok.Value;

@Value
public class PartnerCreatedEvent {
    private final Integer partnerId;
    private final Partner createdPartner;
}
