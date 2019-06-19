package com.millenium.devopsbuddy.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;

public class UserUtils {

	private UserUtils() {
		throw new AssertionError("Non instantiable");
	}
	
	public static User createBasicUser() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		User user = new User();
		user.setUsername("basicUser");
		user.setPassword(encoder.encode("secret"));
		user.setEmail("me@example.com");
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789123");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");
		user.setStripeCustomerId("stripeCustomerId");
		return user;
	}
}
