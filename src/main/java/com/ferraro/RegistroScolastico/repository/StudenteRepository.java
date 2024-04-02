package com.ferraro.RegistroScolastico.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.enums.Materia;

public interface StudenteRepository extends JpaRepository<Studente, Long>{

	Optional<Studente> findByUser_Email(String email);
	
	Optional<Studente> findByAnagrafica_Cf(String cf);
	
	@Query("select AVG(v.voto) from Voto v JOIN v.studente s WHERE materia = ?1 AND periodo = ?2 AND s.id = ?3 ")
	Optional<Double> votoMedioMateria(Materia materia, Periodo periodo, Long id);

	Set<Voto> findVotiByPeriodo(Periodo periodo);
}
