package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PropIntelApplication {

	public static void main(String[] args) {
//		String port = System.getenv("PORT");
//		System.out.println("ENV PORT: " + port);
//		SpringApplication.run(PropIntelApplication.class, args);

		SpringApplication app = new SpringApplication(PropIntelApplication.class);
		Map<String, Object> props = new HashMap<>();
		String port = System.getenv("PORT");
		if (port != null) {
			props.put("server.port", port);
		}
		props.put("server.address", "0.0.0.0");
		app.setDefaultProperties(props);
		app.run(args);
	}

}
