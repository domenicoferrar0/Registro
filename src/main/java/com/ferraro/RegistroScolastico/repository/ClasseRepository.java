package com.ferraro.RegistroScolastico.repository;


import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long>{
	
	public Boolean existsByAnnoAndSezioneAndPeriodo(Integer anno, String sezione, Periodo periodo);
		
	public Optional<Classe> findByAnnoAndSezione(Integer anno, String sezione);
	
	@Query("SELECT c from Classe c WHERE ?1 MEMBER OF c.docenti AND c.periodo = ?2 ORDER BY c.anno ASC, c.sezione ASC")
	public Set<Classe> findByDocenteAndPeriodo(Docente docente, Periodo periodo);
	
	@Query("SELECT c from Classe c")
	public Set<Classe> findAllClasse();
}
