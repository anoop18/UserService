package com.org.userservice.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.org.userservice.entities.Hotel;
import com.org.userservice.entities.Rating;
import com.org.userservice.entities.User;
import com.org.userservice.exception.ResourceNotFoundException;
import com.org.userservice.external.services.HotelService;
import com.org.userservice.external.services.RatingService;
import com.org.userservice.repositories.UserRepository;
import com.org.userservice.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RatingService ratingService;

	@Override
	public User saveUser(User user) {
		String randoMmUserId = UUID.randomUUID().toString();
		user.setUserId(randoMmUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
 
	@Override
	public User getUser(String userId) {
    User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with give id is not found on server !!" + userId));
     
    ResponseEntity<List<Rating>> ratings= ratingService.getAllRatingsByUserId(user.getUserId());
    
    
    ratings.getBody().stream().map(rating->{
    	
    	 Hotel hotel = hotelService.getHotel(rating.getHotelId()); 
    	 rating.setHotel(hotel);
    	 
    	 return rating;
    	    
    	
    }).toList();
    user.setRatings(ratings.getBody());
    
   
    return user;
	}

}
