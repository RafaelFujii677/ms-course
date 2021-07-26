package com.mscourse.hrpayroll.resource;

import java.io.Serializable;
import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mscourse.hrpayroll.model.Payment;
import com.mscourse.hrpayroll.service.PaymentService;

import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.vavr.control.Try;

@RestController
@RequestMapping("/payments")
public class PaymentResource implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/{workerId}/days/{days}")
	public ResponseEntity<Payment> getPayment(@PathVariable Long workerId, @PathVariable Integer days){		
		Supplier<Payment> supplier = () -> paymentService.getPayment(workerId, days);	

		// Config Timeout
		Decorators.ofSupplier(supplier)
			.withRateLimiter(RateLimiter.of("paymentService", RateLimiterConfig.custom().timeoutDuration(Duration.ofMillis(5000)).build()));

		Payment result = Try.ofSupplier(supplier).recover(throwable -> paymentService.alternativeGetPayment(workerId, days)).get();
		return ResponseEntity.ok(result);
	}
	
}
