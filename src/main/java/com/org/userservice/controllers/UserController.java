package com.org.userservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.userservice.entities.User;
import com.org.userservice.services.UserService;

import ch.qos.logback.classic.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		User saveUser = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
	}
	int retryCount=1;
	@GetMapping("/{userId}")
	//@CircuitBreaker(name="ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
	//@Retry(name = "ratingHotelService",fallbackMethod ="ratingHotelFallback" )
	@RateLimiter(name = "userRateLimiter",fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		log.info("Retry Count " + retryCount);
		retryCount++;
		User user = userService.getUser(userId);
		
		return ResponseEntity.ok(user);
	}
	//creating fallback method for circuit breaker  
	
	
	public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
	      ex.printStackTrace();		

		
      log.info("Fallback is executed because service is down : ",ex.getMessage());		
     User user= User.builder()
      .email("dummy@gmail.com")
      .name("Dummy")
     . about("This user is created dummy because some services is down")
     .userId("1234").build();
      return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	
	@GetMapping 
	public ResponseEntity<List<User>> getAllUser(){
		 List<User> allUser = userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}

}
