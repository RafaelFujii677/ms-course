package com.mscourse.hroauth.resource;

import java.io.Serializable;
import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/oauth")
public class OAuthResource implements Serializable{
	private static final long serialVersionUID = 1L;

//	@Autowired
//	private Environment env;

	@GetMapping("/user")
	public Principal getPrincipal(Principal user) {
		return user;
	}

//	@GetMapping("/config")
//	public String getConfig() {
//		return env.getProperty("private-key.config"); 
//	}

}
