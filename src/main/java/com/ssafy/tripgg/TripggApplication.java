package com.ssafy.tripgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TripggApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripggApplication.class, args);
	}

}
