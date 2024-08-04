package com.dda.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Properties;

@SpringBootApplication
@EnableAsync
public class DriveDropAcademyWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriveDropAcademyWebsiteApplication.class, args);
	}

}
