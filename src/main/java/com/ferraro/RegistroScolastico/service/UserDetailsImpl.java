package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.exceptions.UserNotEnabledException;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	

	// DA TOGLIERE
	public List<User> findAll() {
		List<User> list = userRepository.findAll();
		for (User u : list) {
			if (!u.isEnabled()) {
				u.setEnabled(true);
			}
		}
		userRepository.saveAll(list);
		return list;
	}

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		log.info(username);
		log.info("INSIDE MY USER DETAILS");
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found, consider the registration"));

		if (!user.isEnabled()) {
			throw new UserNotEnabledException(username);
		}

		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}

	public String generatePassword() {
		String password = UUID.randomUUID().toString();
		return password;
	}

	

}
