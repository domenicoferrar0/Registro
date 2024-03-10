package com.ferraro.RegistroScolastico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferraro.RegistroScolastico.entities.Studente;

public interface StudenteRepository extends JpaRepository<Studente, Long>{

	Optional<Studente> findByUser_Email(String email);
	
	Optional<Studente> findByAnagrafica_Cf(String cf);
}
