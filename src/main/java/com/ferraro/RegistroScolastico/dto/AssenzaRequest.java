package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssenzaRequest {
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@PastOrPresent(message = "La data non può essere nel futuro")
	private LocalDate data;
	
	@NotBlank
	private String studenteCF;
	
	@Range(min = 1, max = 6)
	@NotNull
	private Integer ore;
		
}
