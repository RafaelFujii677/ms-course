package com.mscourse.hrworker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mscourse.hrworker.feignClients.OauthFeignClient;

@Service
public class OauthService {

	@Autowired
	private OauthFeignClient oauthFeignClient;
	
	public Object authenticate(String token) {
		try {
			return oauthFeignClient.getPrincipal(token);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
