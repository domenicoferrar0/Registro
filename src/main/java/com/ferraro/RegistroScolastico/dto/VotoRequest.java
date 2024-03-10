package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VotoRequest {
	
	@NotNull
	private String studenteCF;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private LocalDate data;
	
	@DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
	@NotNull
	private Double voto;

}
