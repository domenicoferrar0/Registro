package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long>{
	
	@Modifying
	@Query("delete from Voto v where v.id = ?1")
	int removeById(Long id);
}
