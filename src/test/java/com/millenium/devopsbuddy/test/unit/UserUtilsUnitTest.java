package com.millenium.devopsbuddy.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.millenium.devopsbuddy.utils.UserUtils;
import com.millenium.devopsbuddy.web.controller.ForgotMyPasswordController;

public class UserUtilsUnitTest {

	private MockHttpServletRequest mockHttpServletRequest;
	
	@Before
	public void init() {
		mockHttpServletRequest = new MockHttpServletRequest();
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
}
