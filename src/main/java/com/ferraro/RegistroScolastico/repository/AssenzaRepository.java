package com.ferraro.RegistroScolastico.repository;

import java.time.LocalDate;
import java.util.Set;

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
	@Query("delete from Assenza a where a.id = ?1")
	int removeById(Long id);
	
	
	@Query("select a from Assenza a JOIN a.studente s WHERE s = ?1 AND a.data <= ?2")
	public Set<Assenza> searchByStudente(Studente studente, LocalDate startRange);
}
