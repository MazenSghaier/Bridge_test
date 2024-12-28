// src/main/java/com/example/coursemanagement/controller/HealthCheckController.java
package com.example.Courses_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "Application is running!";
    }
}