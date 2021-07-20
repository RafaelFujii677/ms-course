package com.mscourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mscourse.model.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{

}
