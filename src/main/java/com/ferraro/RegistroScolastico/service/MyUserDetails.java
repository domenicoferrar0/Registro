package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.repository.UserRepository;

@Service
public class MyUserDetails implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println(username);
				User user = userRepository.findByEmail(username)
						.orElseThrow(()-> new UsernameNotFoundException(username+" not found, consider the registration"));
				System.out.println(username);
				Set<GrantedAuthority> authorities = user.getRoles().stream()
						.map((role) -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toSet());
				return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}

}
