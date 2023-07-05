package com.example.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication


public class FashionFusionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionFusionApplication.class, args);

	}

}
