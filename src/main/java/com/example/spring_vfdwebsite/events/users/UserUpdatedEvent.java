package com.example.spring_vfdwebsite.events.users;

import com.example.spring_vfdwebsite.entities.User;

import lombok.Value;

@Value
public class UserUpdatedEvent {
    private final Integer userId;
    private final User updatedUser;
}