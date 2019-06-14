package com.millenium.devopsbuddy.test.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.Role;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.millenium.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.millenium.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.millenium.devopsbuddy.backend.persistence.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepositoriesIntegrationTest {

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;
	
	private static final int BASIC_PLAN_ID = 1;
	private static final int BASIC_ROLE_ID = 1;

	@Before
	public void init() {
		assertNotNull(planRepository);
		assertNotNull(roleRepository);
		assertNotNull(userRepository);
	}
	
	@Test
	public void testCreateNewPlan() {
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findById(BASIC_PLAN_ID).get();
		assertNotNull(retrievedPlan);
	}
	
	@Test
	public void testCreateNewRole() {
		Role userRole = createBasicRole();
		roleRepository.save(userRole);
		Role retrievedRole = roleRepository.findById(BASIC_ROLE_ID).get();
		assertNotNull(retrievedRole);
	}
	
	@Test
	public void createNewTest() throws Exception {
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		
		User basicUser = createBasicUser();
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createBasicRole();
		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole();
		userRole.setRole(basicRole);
		userRole.setUser(basicUser);
		userRoles.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoles);
		
		for (UserRole ur : userRoles) {
			roleRepository.save(ur.getRole());
		}
		
		basicUser = userRepository.save(basicUser);
		
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
	
	//------------------------ Private methods
	
	private Plan createBasicPlan() {
		Plan plan = new Plan();
		plan.setId(BASIC_PLAN_ID);
		plan.setName("Basic");
		return plan;
	}
	
	private Role createBasicRole() {
		Role role = new Role();
		role.setId(BASIC_ROLE_ID);
		role.setName("ROLE_USER");
		return role;
	}
	
	private User createBasicUser() {
		User user = new User();
		user.setUsername("basicUser");
		user.setPassword("secret");
		user.setEmail("me@example.com");
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789123");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");
		return user;
	}
}
