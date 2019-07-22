package com.millenium.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.Role;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.millenium.devopsbuddy.backend.service.UserService;
import com.millenium.devopsbuddy.enums.PlansEnum;
import com.millenium.devopsbuddy.enums.RolesEnum;
import com.millenium.devopsbuddy.utils.UserUtils;

public abstract class AbstractServiceIntegrationTest {

	@Autowired
	protected UserService userService;

	protected User createUser(TestName testName) {
		String username = testName.getMethodName();
		String email = testName.getMethodName() + "@devopsbuddy.com";
		
		Set<UserRole> userRoles = new HashSet<>();
		User basicUser = UserUtils.createBasicUser(username, email);
		userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
		
		return userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
	}

}
