package com.devsuperior.hrpayroll.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsuperior.hrpayroll.model.Payment;
import com.devsuperior.hrpayroll.model.Worker;

@Service
public class PaymentService {
	
	@Value("${hr-worker.host}")
	private String wokerHost;
	
	@Autowired
	private RestTemplate restTemplete;
	
	public Payment getPayment(Long workerId, Integer days) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("id", ""+workerId);
		
		Worker worker = restTemplete.getForObject(wokerHost + "/workers/{id}", Worker.class, uriVariables);
		return new Payment(worker.getName(), worker.getDailyIncome(), days);
	}
}
