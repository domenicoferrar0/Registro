package com.ferraro.RegistroScolastico.dto;

import com.ferraro.RegistroScolastico.enums.Materia;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DocenteDTO {
	
	private Long id;
	
	private AnagraficaDTO anagrafica;
	
	private String email;
	
	private Set<Materia> materia;
	
	private Set<ClasseDTO> classi;
}
