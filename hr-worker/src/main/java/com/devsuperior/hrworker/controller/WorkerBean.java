package com.devsuperior.hrworker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.hrworker.model.Worker;
import com.devsuperior.hrworker.repositories.WorkerDAO;

import java.util.List;

@RestController
@RequestMapping(value = "/workers")
public class WorkerBean {

	@Autowired
	private WorkerDAO workerDAO;
	
	@GetMapping
	public ResponseEntity<List<Worker>> findAll(){
		List<Worker> list = workerDAO.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Worker> findById(@PathVariable Long id){
		Worker obj = workerDAO.findById(id).get();
		return ResponseEntity.ok(obj);
	}
}
