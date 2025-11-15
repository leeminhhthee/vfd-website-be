package com.example.spring_vfdwebsite.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BackgroundService {
    @Async
    public void performBackgroundTask() {
        System.out.println("⏰ Executing task in thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000); // Simulate long-running task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("✅ Task completed in thread: " + Thread.currentThread().getName());
    }
}
