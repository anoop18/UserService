package com.org.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

}
