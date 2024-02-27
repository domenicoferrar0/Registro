package com.ferraro.RegistroScolastico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long>{
	
	public Boolean existsByAnnoAndSezione(Integer anno, String sezione);
		
	public Optional<Classe> findByAnnoAndSezione(Integer anno, String sezione);
}
