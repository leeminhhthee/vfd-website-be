package com.example.spring_vfdwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringVfdwebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringVfdwebsiteApplication.class, args);
	}

}
