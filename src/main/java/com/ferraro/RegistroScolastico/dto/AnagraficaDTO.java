package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Genere;
import com.ferraro.RegistroScolastico.enums.Provincia;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AnagraficaDTO {
	
	private Long id;
	
	
	private String nome;
	
	
	private String cognome;
	
	

	private LocalDate nascita;
	
	private String cf;
	
	
	private String indirizzo;
		
	
	
	private Genere genere;
	
	
	private Provincia luogoDiNascita;
}
