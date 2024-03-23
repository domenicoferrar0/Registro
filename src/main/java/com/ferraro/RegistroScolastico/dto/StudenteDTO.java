package com.ferraro.RegistroScolastico.dto;

import java.util.Set;

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
	
	private ClasseDTO classe;
	
	private Set<AssenzaDTO> assenze;
	
	private Set<VotoDTO> voti;
		
}
