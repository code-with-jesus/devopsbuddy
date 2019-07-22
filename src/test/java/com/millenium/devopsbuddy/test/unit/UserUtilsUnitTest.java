package com.millenium.devopsbuddy.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.utils.UserUtils;
import com.millenium.devopsbuddy.web.controller.ForgotMyPasswordController;
import com.millenium.devopsbuddy.web.domain.frontend.BasicAccountPayload;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class UserUtilsUnitTest {

	private MockHttpServletRequest mockHttpServletRequest;
	
	private PodamFactory podamFactory;
	
	@Before
	public void init() {
		mockHttpServletRequest = new MockHttpServletRequest();
		podamFactory = new PodamFactoryImpl();
	}
	
	@Test
	public void testPasswordResetEmailUrlConstruction() throws Exception {
		mockHttpServletRequest.setServerPort(8080);
		
		String token = UUID.randomUUID().toString();
		long userId = 123456;
		
		String expectedUrl = "http://localhost:8080/" + ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId + "&token=" + token;
		String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	@Test
	public void mapWebUserToDomainUser() {
		BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
		webUser.setEmail("me@example.com");
		
		User user = UserUtils.fromWebUserToDomainUser(webUser);
		assertNotNull(user);
		
		assertEquals(webUser.getUsername(), user.getUsername());
		assertEquals(webUser.getPassword(), user.getPassword());
		assertEquals(webUser.getFirstName(), user.getFirstName());
		assertEquals(webUser.getLastName(), user.getLastName());
		assertEquals(webUser.getEmail(), user.getEmail());
		assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
		assertEquals(webUser.getCountry(), user.getCountry());
		assertEquals(webUser.getDescription(), user.getDescription());
	}
}
