package com.millenium.devopsbuddy.test.unit;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;

import com.millenium.devopsbuddy.test.integration.StripeIntegrationTest;
import com.millenium.devopsbuddy.utils.StripeUtils;
import com.millenium.devopsbuddy.web.domain.frontend.ProAccountPayload;

public class StripeUtilsUnitTest {

	@Test
	@SuppressWarnings("unchecked")
	public void createStripeTokenParamsFromUserPayload() {
		ProAccountPayload payload = new ProAccountPayload();
		String cardNumber = StripeIntegrationTest.TEST_CC_NUMBER;
		payload.setCardNumber(cardNumber);
		String cardCode = StripeIntegrationTest.TEST_CC_CVC_NBR;
		payload.setCardCode(cardCode);
		String cardMonth = String.valueOf(StripeIntegrationTest.TEST_CC_EXP_MONTH);
		payload.setCardMonth(cardMonth);
		String cardYear = String.valueOf(LocalDate.now(Clock.systemUTC()).getYear() + 1);
		payload.setCardYear(cardYear);
		
		Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
		Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
		
		assertEquals(cardNumber, cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY));
		assertEquals(cardMonth, String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_MONTH_KEY)));
		assertEquals(cardYear, String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_YEAR_KEY)));
		assertEquals(cardCode, cardParams.get(StripeUtils.STRIPE_CVC_KEY));
	}
}
