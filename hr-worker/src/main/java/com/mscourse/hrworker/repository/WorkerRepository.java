package com.mscourse.hrworker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mscourse.hrworker.model.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{

}
