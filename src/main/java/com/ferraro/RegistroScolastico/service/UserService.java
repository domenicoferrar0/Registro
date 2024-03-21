package com.ferraro.RegistroScolastico.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;

	
	
	@Transactional
	public boolean changePw(String email, LoginDTO loginDTO) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(email));
		if(!email.equals(loginDTO.getEmail())) {
			return false;
		}
		if (!encoder.matches(loginDTO.getPassword(), user.getPassword())){
			return false;
		}
		
		String newPassword = encoder.encode(loginDTO.getPassword());
		user.setPassword(newPassword);
		userRepository.save(user);
		return true;
		
	}
}
