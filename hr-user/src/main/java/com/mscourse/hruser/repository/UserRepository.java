package com.mscourse.hruser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mscourse.hruser.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
}
