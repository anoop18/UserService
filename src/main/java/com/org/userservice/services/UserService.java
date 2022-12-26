package com.org.userservice.services;

import java.util.List;

import com.org.userservice.entities.User;

public interface UserService {
	
	User saveUser(User user);
	
	List<User> getAllUser();
	
	User  getUser(String userId);

}
