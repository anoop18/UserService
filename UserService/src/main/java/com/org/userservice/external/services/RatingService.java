package com.org.userservice.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.org.userservice.entities.Rating;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {
	
	@PostMapping("api/v1/ratings")
	ResponseEntity<Rating> createRating(Rating values);

	
	@PutMapping("api/v1/ratings/{ratingId}")
	ResponseEntity<Rating> updateRating(@PathVariable String ratingId, Rating rating);
	
	@DeleteMapping("api/v1/ratings/{ratingId}")
	void deleteRating(@PathVariable String ratingId);
	
}
