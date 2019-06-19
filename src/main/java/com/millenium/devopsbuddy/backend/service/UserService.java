package com.millenium.devopsbuddy.backend.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;
import com.millenium.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.millenium.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.millenium.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.millenium.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.millenium.devopsbuddy.enums.PlansEnum;

@Service
@Transactional(readOnly = true)
public class UserService {

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {
		
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		
		Plan plan = new Plan(plansEnum);
		// It makes sure the plans exists in the database
		if (!planRepository.existsById(plansEnum.getId())) {
			planRepository.save(plan);
		}
		user.setPlan(plan);
		for (UserRole ur : userRoles) {
			roleRepository.save(ur.getRole());
		}
		user.getUserRoles().addAll(userRoles);
		// flush referenced objects before the user is saved
		em.flush();
		user = userRepository.save(user);
		return user;
	}
	
}
