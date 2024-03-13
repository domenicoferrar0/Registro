package com.ferraro.RegistroScolastico.dto;

import java.util.Set;

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
}
