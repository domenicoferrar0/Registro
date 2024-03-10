package com.ferraro.RegistroScolastico.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Studente;

@Repository
public interface AssenzaRepository extends JpaRepository<Assenza, Long>{

	public Boolean existsByStudenteAndData(Studente studente, LocalDate data);
	
	@Modifying
	@Query("delete from assenza a where a.id = 1?")
	Boolean removeById(Long id);
}
