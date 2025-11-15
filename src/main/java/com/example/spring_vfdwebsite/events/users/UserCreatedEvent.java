package com.example.spring_vfdwebsite.events.users;

import com.example.spring_vfdwebsite.entities.User;

import lombok.Value;

@Value
public class UserCreatedEvent {
    private final Integer userId;
    private final User createdUser;
}
