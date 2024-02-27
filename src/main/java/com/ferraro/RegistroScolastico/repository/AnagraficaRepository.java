package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Anagrafica;

@Repository
public interface AnagraficaRepository extends JpaRepository<Anagrafica, Long>{
	
	public Boolean existsByCf(String cf);
}
