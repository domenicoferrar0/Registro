package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VotoRequest {
	
	@NotNull
	@Min(value = 0, message = "Inserisci uno studente valido")
	private Long studentId;
	
	@PastOrPresent(message = "La data non può essere nel futuro")
	@Temporal(TemporalType.DATE)
	@NotNull
	private LocalDate data;
	
	@DecimalMin(value = "0.0", message = "il voto deve essere compreso tra 0 e 10")
    @DecimalMax(value = "10.0", message = "il voto deve essere compreso tra 0 e 10")
	@NotNull
	private Double voto;

}
