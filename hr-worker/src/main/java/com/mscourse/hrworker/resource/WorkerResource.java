package com.mscourse.hrworker.resource;

import java.io.Serializable;
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

import com.mscourse.hrworker.model.Worker;
import com.mscourse.hrworker.repository.WorkerRepository;
import com.mscourse.hrworker.service.OauthService;

@RefreshScope
@RestController
@RequestMapping(value = "/workers")
public class WorkerResource implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private WorkerRepository workerRepository;
	
	@Autowired
	private OauthService oauthService;

	@GetMapping("/all")
	public ResponseEntity<List<Worker>> findAll(){
		List<Worker> workerList = workerRepository.findAll();
		return workerList != null ? ResponseEntity.ok(workerList) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Worker> findById(@PathVariable Long id){
		Worker worker = workerRepository.findById(id).get();
		return worker != null ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
	}

	@GetMapping("/testToken")
	public ResponseEntity<?> testToken(@RequestHeader("Authorization") String token){
		try {
			oauthService.authenticate(token);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
