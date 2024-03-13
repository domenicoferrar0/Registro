package com.ferraro.RegistroScolastico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.entities.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long>{
	
	public Boolean existsByAnnoAndSezioneAndPeriodo(Integer anno, String sezione, Periodo periodo);
		
	public Optional<Classe> findByAnnoAndSezione(Integer anno, String sezione);
}
