package com.ferraro.RegistroScolastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.entities.Role;
import com.ferraro.RegistroScolastico.exceptions.RoleNotFoundException;
import com.ferraro.RegistroScolastico.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Role findByName(String name) {
		return roleRepository.findByName(name)
				.orElseThrow(()-> new RoleNotFoundException(name));
	}
}
