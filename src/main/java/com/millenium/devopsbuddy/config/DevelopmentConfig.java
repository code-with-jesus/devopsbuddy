package com.millenium.devopsbuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.millenium.devopsbuddy.backend.service.EmailService;
import com.millenium.devopsbuddy.backend.service.MockEmailService;

@Configuration
@Profile("dev")
@PropertySource("file:///${user.home}/.devopsbuddy/application-dev.properties")
public class DevelopmentConfig {

	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
