package com.ev.ocpp16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OcppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OcppApplication.class, args);
	}

}
