package com.millenium.devopsbuddy.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.service.EmailService;
import com.millenium.devopsbuddy.backend.service.PasswordResetTokenService;
import com.millenium.devopsbuddy.utils.UserUtils;
import com.millenium.devopsbuddy.web.i18n.I18NService;

@Controller
public class ForgotMyPasswordController {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);
	
	public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
	
	public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";
	
	public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
	
	public static final String MAIL_SENT_KEY = "mailSent";
	
	public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";
	
	@Autowired
	private I18NService i18NService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${webmaster.email}")
	private String webMasterEmail;
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	
	@GetMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordGet() {
		return EMAIL_ADDRESS_VIEW_NAME;
	}
	
	@PostMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordPost(HttpServletRequest request, 
									 @RequestParam("email") String email, 
									 ModelMap model) {
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);
		if (null == passwordResetToken) {
			LOG.debug("Couldn't find a password reset token for email {}", email);
		} else {
			User user = passwordResetToken.getUser();
			String token = passwordResetToken.getToken();
			
			String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
			LOG.debug("Reset Password URL {}", resetPasswordUrl);
			
			String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("[DevOpsBuddy]: How to Reset Your Password");
			mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
			mailMessage.setFrom(webMasterEmail);
			
			emailService.sendGenericEmailMessage(mailMessage);
		}
		model.addAttribute(MAIL_SENT_KEY, true);
		return EMAIL_ADDRESS_VIEW_NAME;
	}
}