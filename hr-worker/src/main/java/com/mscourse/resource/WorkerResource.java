package com.mscourse.resource;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	private Environment env;
	
	@GetMapping("/configs")
	public ResponseEntity<String> getConfigs(){
		return ResponseEntity.ok(env.getProperty("test.config"));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> findAll(){
		List<Worker> list = workerRepository.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Worker obj = workerRepository.findById(id).get();
		return ResponseEntity.ok(obj);
	}
}
