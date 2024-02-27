package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public Boolean existsByEmail(String email);
	
}
