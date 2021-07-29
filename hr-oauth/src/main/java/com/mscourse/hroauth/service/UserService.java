package com.mscourse.hroauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mscourse.hroauth.feignclients.UserFeignClient;
import com.mscourse.hroauth.model.User;

import feign.FeignException;

@Service
public class UserService {

	@Autowired
	private UserFeignClient userFeignClient;

	public User findByEmail(String email) {
		try {
			return userFeignClient.findByEmail(email).getBody();			
		} catch (FeignException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
