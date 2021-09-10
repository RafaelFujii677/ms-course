package com.mscourse.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "hr-oauth", path = "/oauth")
public interface OauthFeignClient {

	@GetMapping(value = "/user")
	public Object getPrincipal(@RequestHeader("Authorization") String token);
}
