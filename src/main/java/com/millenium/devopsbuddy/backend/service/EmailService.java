package com.millenium.devopsbuddy.backend.service;

import org.springframework.mail.SimpleMailMessage;

import com.millenium.devopsbuddy.web.domain.frontend.FeedbackPojo;

public interface EmailService {

	/**
	 * Send an email with the content in the Feedback Pojo.
	 * @param feedbackPojo The Feedback Pojo
	 */
	public void sendFeedbackEmail(FeedbackPojo feedbackPojo);
	
	/**
	 * Send an email with the content of the Simple Mail Message object.
	 * @param message The object containing the email content
	 */
	public void sendGenericEmailMessage(SimpleMailMessage message);
}
