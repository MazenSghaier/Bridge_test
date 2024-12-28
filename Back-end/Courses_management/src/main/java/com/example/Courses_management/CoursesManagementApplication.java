package com.example.Courses_management;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class CoursesManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoursesManagementApplication.class, args);
	}
}
