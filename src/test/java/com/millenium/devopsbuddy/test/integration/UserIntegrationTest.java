package com.millenium.devopsbuddy.test.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.Role;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.millenium.devopsbuddy.enums.PlansEnum;
import com.millenium.devopsbuddy.enums.RolesEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest extends AbstractIntegrationTest {
	
	@Rule
	public TestName testName = new TestName();

	@Before
	public void init() {
		assertNotNull(planRepository);
		assertNotNull(roleRepository);
		assertNotNull(userRepository);
	}
	
	@Test
	public void testCreateNewPlan() {
		Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId()).get();
		assertNotNull(retrievedPlan);
	}
	
	@Test
	public void testCreateNewRole() {
		Role userRole = createRole(RolesEnum.BASIC);
		roleRepository.save(userRole);
		Role retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId()).get();
		assertNotNull(retrievedRole);
	}
	
	@Test
	public void createNewUser() throws Exception {
		User basicUser = createUser(testName);
		User newlyCreatedUser = userRepository.findById(basicUser.getId()).get();
		
		assertNotNull(newlyCreatedUser);
		assertTrue(newlyCreatedUser.getId() != 0);
		assertNotNull(newlyCreatedUser.getPlan());
		assertNotNull(newlyCreatedUser.getPlan().getId());
		Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.getUserRoles();
		for (UserRole ur : newlyCreatedUserRoles) {
			assertNotNull(ur.getRole());
			assertNotNull(ur.getRole().getId());
		}
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		User basicUser = createUser(testName);
		userRepository.deleteById(basicUser.getId());
	}
	
}
