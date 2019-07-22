package com.millenium.devopsbuddy.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.Role;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.millenium.devopsbuddy.backend.service.PlanService;
import com.millenium.devopsbuddy.backend.service.S3Service;
import com.millenium.devopsbuddy.backend.service.UserService;
import com.millenium.devopsbuddy.enums.PlansEnum;
import com.millenium.devopsbuddy.enums.RolesEnum;
import com.millenium.devopsbuddy.utils.UserUtils;
import com.millenium.devopsbuddy.web.domain.frontend.ProAccountPayload;

@Controller
public class SignupController {
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private S3Service s3Service;
	
	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);
	
	public static final String SIGNUP_URL_MAPPING = "/signup";
	
	public static final String PAYLOAD_MODEL_KEY_NAME = "payload";
	
	public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";
	
	public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
	
	public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
	
	public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
	
	public static final String ERROR_MESSAGE_KEY = "message";
	
	@GetMapping(SIGNUP_URL_MAPPING)
	public String signUpGet(@RequestParam("planId") int planId, ModelMap model) {
		
		if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
			throw new IllegalArgumentException("Plan id is not valid");
		}
		model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
		return SUBSCRIPTION_VIEW_NAME;
	}
	
	@PostMapping(SIGNUP_URL_MAPPING)
	public String signUpPost(@RequestParam(name = "planId", required = true) int planId, 
							 @RequestParam(name = "file", required = false) MultipartFile file,
							 @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload, 
							 ModelMap model) throws IOException {
		
		if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
			return SUBSCRIPTION_VIEW_NAME;
		}
		this.checkForDuplicates(payload, model);
		
		boolean duplicates = false;
		
		List<String> errorMessages = new ArrayList<>();
		
		if (model.containsKey(DUPLICATED_USERNAME_KEY)) {
			LOG.warn("The username already exists. Displaying error to the user");
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			errorMessages.add("Username already exists");
			duplicates = true;
		}
		if (model.containsKey(DUPLICATED_EMAIL_KEY)) {
			LOG.warn("The email already exists. Displaying error to the user");
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			errorMessages.add("Email already exists");
			duplicates = true;
		}
		if (duplicates) {
			model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
			return SUBSCRIPTION_VIEW_NAME;
		}
		
		// There are certain info that the user doesn't set, such as profile image URL, Stripe customer id, plans and roles
		LOG.debug("Transforming user payload into User domain object");
		User user = UserUtils.fromWebUserToDomainUser(payload);
		
		if (file != null && !file.isEmpty()) {
			String profileImageUrl = s3Service.storeProfileImage(file, payload.getUsername());
			if (profileImageUrl != null) {
				user.setProfileImageUrl(profileImageUrl);
			} else {
				LOG.warn("There was a problem uploading the profile image to S3. The user's profile will be created without the image");
			}
		}
		
		// Sets the plans and the roles
		Plan selectedPlan = planService.finPlanById(planId);
		if (null == selectedPlan) {
			LOG.error("The plan id {} could not be found. Throwing exception.", planId);
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
		}
		
		user.setPlan(selectedPlan);
		
		User registeredUser = null;
		Set<UserRole> roles = new HashSet<>();
		if (planId == PlansEnum.BASIC.getId()) {
			roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
			registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
		} else {
			roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
			registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
			LOG.debug(payload.toString());
		}
		
		
		// Auto logins the registered user
		Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null, registeredUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		LOG.info("User created successfully");
		
		model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");
		
		return SUBSCRIPTION_VIEW_NAME;
	}

	//------------------- Private methods
	
	/**
	 * Checks if the username/email are duplicates and set error flags in the model
	 * @param payload
	 * @param model
	 */
	private void checkForDuplicates(@Valid ProAccountPayload payload, ModelMap model) {
		if (userService.findByUserName(payload.getUsername()) != null) {
			model.addAttribute(DUPLICATED_USERNAME_KEY, true);
		}
		if (userService.findByEmail(payload.getEmail()) != null) {
			model.addAttribute(DUPLICATED_EMAIL_KEY, true);
		}
	}
}
