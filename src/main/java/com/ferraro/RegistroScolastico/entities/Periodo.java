package com.ferraro.RegistroScolastico.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Periodo {
	
	@NotNull
	int startYear;
	
	@NotNull
	int finishYear;
	
}
