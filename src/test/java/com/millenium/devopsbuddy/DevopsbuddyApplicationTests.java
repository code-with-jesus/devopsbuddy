package com.millenium.devopsbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.millenium.devopsbuddy.web.i18n.I18NService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DevopsbuddyApplicationTests {

	@Autowired
	private I18NService i18nService;
	
	@Test
	public void testMessagesByLocaleService() throws Exception {
		String expectedResult = "Bootstrap starter template";
		String messageId = "index.main.callout";
		String actual = i18nService.getMessage(messageId);
		assertEquals("The actual and expected Strings don't match", expectedResult, actual);
	}

}
