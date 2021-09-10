package com.mscourse.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mscourse.feignClients.OauthFeignClient;
import com.mscourse.model.Worker;
import com.mscourse.repository.WorkerRepository;

@RefreshScope
@RestController
@RequestMapping(value = "/workers")
public class WorkerResource implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private WorkerRepository workerRepository;
	@Autowired
	private OauthFeignClient oauthFeignClient;

	@GetMapping("/all")
	public ResponseEntity<List<Worker>> findAll(@RequestHeader("Authorization") String token){
		List<Worker> list = new ArrayList<>();
		try { // TODO gambiarra
			if(oauthFeignClient.getPrincipal(token) != null) list = workerRepository.findAll();
		} catch (Exception e) {
			System.out.println("Invalid Token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Worker> findById(@PathVariable Long id){
		Worker worker = workerRepository.findById(id).get();
		return worker != null ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
	}
}
