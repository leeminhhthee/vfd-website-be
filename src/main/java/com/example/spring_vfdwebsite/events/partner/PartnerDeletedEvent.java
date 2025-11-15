package com.example.spring_vfdwebsite.events.partner;

import lombok.Value;

@Value
public class PartnerDeletedEvent {
    private final Integer partnerId;
}
