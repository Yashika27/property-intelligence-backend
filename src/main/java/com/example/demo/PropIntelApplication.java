package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PropIntelApplication {

	public static void main(String[] args) {
		String port = System.getenv("PORT");
		System.out.println("ENV PORT: " + port);
		SpringApplication.run(PropIntelApplication.class, args);
	}

}
