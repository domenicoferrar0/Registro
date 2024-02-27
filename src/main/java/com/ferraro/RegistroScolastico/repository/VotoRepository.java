package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long>{

}
