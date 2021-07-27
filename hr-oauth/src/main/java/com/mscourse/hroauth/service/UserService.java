package com.mscourse.hroauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mscourse.hroauth.feignclients.UserFeignClient;
import com.mscourse.hroauth.model.User;

@Service
public class UserService {

	@Autowired
	private UserFeignClient userFeignClient;

	public User findByEmail(String email) {
		return userFeignClient.findByEmail(email).getBody();
	}
}
