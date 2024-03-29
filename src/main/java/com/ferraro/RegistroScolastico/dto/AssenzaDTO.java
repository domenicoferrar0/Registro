package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssenzaDTO {
	
	private Long id;
	
	private LocalDate data;
	
	private Integer ore;
	
	private StudenteDTOSimple studente;
	
	
}
