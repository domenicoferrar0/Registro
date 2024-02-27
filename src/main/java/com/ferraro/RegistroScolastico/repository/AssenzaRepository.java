package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Assenza;

@Repository
public interface AssenzaRepository extends JpaRepository<Assenza, Long>{

}
