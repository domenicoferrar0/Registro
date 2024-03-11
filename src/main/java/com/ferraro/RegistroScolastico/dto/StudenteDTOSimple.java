package com.ferraro.RegistroScolastico.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudenteDTOSimple {
	
	private Long id;
	
	private String nome;
	
	private String cognome;
	
	private String cf;
	
	
}
