package com.ferraro.RegistroScolastico.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long>{
	
	@Modifying
	@Query("delete from Voto v where v.id = ?1")
	int removeById(Long id);
	//DA CONTROLLARE SE NON CERCA ALTRI STUDENTI
	@Query("select v from Voto v JOIN v.docente d JOIN v.studente s WHERE s = ?2 AND CAST(d.materia as string) LIKE %?1% OR CAST (v.voto as string) = ?1")
	Set<Voto> searchByStudente(String searchTerm, Studente studente);
}
