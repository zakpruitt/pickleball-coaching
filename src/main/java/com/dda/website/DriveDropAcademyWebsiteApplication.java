package com.dda.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DriveDropAcademyWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriveDropAcademyWebsiteApplication.class, args);
	}

}
