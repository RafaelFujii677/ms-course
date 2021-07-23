package com.mscourse.hrpayroll.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mscourse.hrpayroll.feignclients.WorkerFeignClients;
import com.mscourse.hrpayroll.model.Payment;
import com.mscourse.hrpayroll.model.Worker;

@Service
public class PaymentService implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private WorkerFeignClients workerFeignClients;

	public Payment getPayment(Long workerId, Integer days) {
		Worker worker = workerFeignClients.findById(workerId).getBody();
		return new Payment(worker.getName(), worker.getDailyIncome(), days);
	}

	public Payment alternativeGetPayment(Long workerId, Integer days) {
		return new Payment("Falha", 500.0, days);
	}
}
