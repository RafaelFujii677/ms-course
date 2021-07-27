package com.mscourse.hruser.resource;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mscourse.hruser.model.User;
import com.mscourse.hruser.repository.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserResource implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Environment env;
	
	@GetMapping("/configs")
	public ResponseEntity<String> getConfigs(){
		return ResponseEntity.ok(env.getProperty("test.config"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id){
		return ResponseEntity.ok(userRepository.findById(id).get());
	}

	@GetMapping("/search")
	public ResponseEntity<User> findByEmail(@RequestParam String email){
		return ResponseEntity.ok(userRepository.findByEmail(email));
	}
}
