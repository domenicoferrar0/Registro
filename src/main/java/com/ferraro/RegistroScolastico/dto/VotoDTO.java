package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class VotoDTO {
	
	private Long id;
		
	
	private StudenteDTO studente;
	
	
	private LocalDate data;
		
	
	private DocenteDTO docente;
	
	
	private Double voto;
}
