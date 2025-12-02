package com.example.spring_vfdwebsite.events.bank;

import com.example.spring_vfdwebsite.entities.Bank;
import lombok.Value;

@Value
public class BankUpdatedEvent {
    private final Integer bankId;
    private final Bank updatedBank;
}
