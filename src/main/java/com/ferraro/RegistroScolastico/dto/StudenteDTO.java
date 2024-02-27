package com.ferraro.RegistroScolastico.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class StudenteDTO {

	private Long id;
	
	private AnagraficaDTO anagrafica;
	
	private String email;
		
}
