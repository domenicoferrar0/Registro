package com.ferraro.RegistroScolastico.dto;

import com.ferraro.RegistroScolastico.enums.Materia;

import java.time.LocalDate;

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
	
	private LocalDate nascita;
	
	
	
	
}
