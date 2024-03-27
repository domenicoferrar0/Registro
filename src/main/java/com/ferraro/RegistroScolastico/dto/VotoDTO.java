package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Materia;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VotoDTO {

	private Long id;

	private StudenteDTOSimple studente;

	private LocalDate data;

	private DocenteDTOSimple docente;

	private Double voto;
	
	private Materia materia;
}
