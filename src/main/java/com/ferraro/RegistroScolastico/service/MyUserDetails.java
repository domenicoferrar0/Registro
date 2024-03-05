package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MyUserDetails implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		log.info(username);
		log.info("INSIDE MY USER DETAILS");
				User user = userRepository.findByEmail(username)
						.orElseThrow(()-> new UsernameNotFoundException(username+" not found, consider the registration"));
				
				Set<GrantedAuthority> authorities = user.getRoles().stream()
						.map((role) -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toSet());
				return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}
	
	public String generatePassword() {
		String password = UUID.randomUUID().toString();
		return password;
	}

}
