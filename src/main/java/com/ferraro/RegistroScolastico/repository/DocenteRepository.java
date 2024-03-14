package com.ferraro.RegistroScolastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Periodo;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long>{

	Optional<Docente> findByAnagrafica_Cf(String cf);
	
	Optional<Docente> findByUser_Email(String email);
	
//	List<Classe> findAllClassiByPeriodoAndDocente(Periodo periodo, Docente docente);
	
}
