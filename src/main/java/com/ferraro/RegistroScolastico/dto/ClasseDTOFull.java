package com.ferraro.RegistroScolastico.dto;

import java.util.Set;

import com.ferraro.RegistroScolastico.entities.Periodo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasseDTOFull {

	private Long id;

	private Integer anno;

	private String sezione;

	private Set<StudenteDTOSimple> studenti;

	private Set<DocenteDTOSimple> docenti;
	
	private Periodo periodo;
}
