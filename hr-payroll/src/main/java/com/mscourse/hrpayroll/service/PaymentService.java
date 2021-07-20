package com.mscourse.hrpayroll.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.mscourse.hrpayroll.model.Payment;

@Service
public class PaymentService implements Serializable{
	private static final long serialVersionUID = 1L;

	public Payment getPayment(Long workerId, Integer days) {
		 return new Payment("Bob", 200.0, days);
	}
}
