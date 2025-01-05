package com.johnmanko.example.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * You can add scanBasePackages to SpringBootApplication if code fall outside of this main class package.
 */
@SpringBootApplication
public class SpringBootHelloServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHelloServiceApplication.class, args);
	}

}
