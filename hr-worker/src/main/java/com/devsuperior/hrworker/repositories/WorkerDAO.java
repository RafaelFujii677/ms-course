package com.devsuperior.hrworker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.hrworker.model.Worker;

public interface WorkerDAO extends JpaRepository<Worker, Long> {

}
