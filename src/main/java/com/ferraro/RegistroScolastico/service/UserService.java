package com.ferraro.RegistroScolastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public User findByEmail(String email) {
		return userRepo.findByEmail(email)
				.orElse(null);
	}
	
}
