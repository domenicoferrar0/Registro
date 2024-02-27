package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferraro.RegistroScolastico.entities.Studente;

public interface StudenteRepository extends JpaRepository<Studente, Long>{

	
}
