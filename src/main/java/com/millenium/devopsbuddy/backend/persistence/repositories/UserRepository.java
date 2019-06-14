package com.millenium.devopsbuddy.backend.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.millenium.devopsbuddy.backend.persistence.domain.backend.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
