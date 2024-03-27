package com.ferraro.RegistroScolastico.dto;

import java.util.Set;

import com.ferraro.RegistroScolastico.enums.Materia;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocenteDTOSimple {
	
	private Long id;

	private String nome;

	private String cognome;

	private String cf;
	
	private Set<Materia> materia;

}
