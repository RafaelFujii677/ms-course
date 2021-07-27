package com.mscourse.hroauth.resource;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mscourse.hroauth.model.User;
import com.mscourse.hroauth.service.UserService;

import feign.FeignException;

@RestController
@RequestMapping(value = "/users")
public class UserResource implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired private UserService service;

	@GetMapping(value = "/search")
	public ResponseEntity<User> findByEmail(@RequestParam String email){
		try {
			User user = service.findByEmail(email);
			return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
		} catch (FeignException illegalArgumentException) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
