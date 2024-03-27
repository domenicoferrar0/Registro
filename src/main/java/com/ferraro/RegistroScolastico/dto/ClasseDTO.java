package com.ferraro.RegistroScolastico.dto;

import java.util.Map;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.enums.Materia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClasseDTO {
	
	
	private Long id;
	
	@NotNull(message = "Anno richiesto")
	@Range(min = 1, max = 3, message = "Gli anni scolastici vanno da 1 a 3")
	private Integer anno;

	@NotBlank(message = "Sezione richiesta")
	@Pattern(regexp = "^[A-Z]$", message = "La sezione è identificata da una singola lettera dell'alfabeto maiuscola")
	private String sezione;
	
	private Periodo periodo;
	
	private Map<Materia, Docente> materieAssegnate;
}
