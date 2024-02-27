package com.ferraro.RegistroScolastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ferraro.RegistroScolastico.entities.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long>{

}
