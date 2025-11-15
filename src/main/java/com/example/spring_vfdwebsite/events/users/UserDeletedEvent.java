package com.example.spring_vfdwebsite.events.users;

import lombok.Value;

@Value
public class UserDeletedEvent {
    private final Integer userId;
}
