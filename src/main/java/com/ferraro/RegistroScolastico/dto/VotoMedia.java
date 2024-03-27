package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Materia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VotoMedia {

	private StudenteDTOSimple studente;
	
	private LocalDate data;
	
	private Double votoMedio;
	
	private Materia materia;
}
