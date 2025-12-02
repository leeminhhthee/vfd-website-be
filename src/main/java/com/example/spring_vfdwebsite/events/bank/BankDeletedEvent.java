package com.example.spring_vfdwebsite.events.bank;

import lombok.Value;

@Value
public class BankDeletedEvent {
    private final Integer bankId;
}
