package com.mscourse.hrworker.service;
//package com.mscourse.service;
//
//import java.security.Principal;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mscourse.feignClients.OauthFeignClient;
//
//@Service
//public class OauthService {
//
//	@Autowired
//	private OauthFeignClient oauthFeignClient;
//	
//	public Principal authenticate(String token) {
//		try {
//			return oauthFeignClient.getPrincipal(token);			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
